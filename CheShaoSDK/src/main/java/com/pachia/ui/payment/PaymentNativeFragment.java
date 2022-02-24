package com.pachia.ui.payment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.cmd.CmdPaymentV3;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.game.BaseDialogFragment;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.InitalizePuchaseObj;
import com.pachia.comon.object.ItemPayObj;
import com.pachia.comon.object.ListPurchaseHistoryObj;
import com.pachia.comon.object.VerifyPurchaseObj;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.DialogUtils;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.PurchaseUtils;
import com.pachia.comon.utils.ToastUtils;
import com.pachia.comon.utils.Utils;
import com.supho.ChelShaoHelper;
import com.quby.R;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.object.PurchaseHistoryObj;
import com.pachia.comon.object.err.GetListPurchaseErrObj;
import com.pachia.comon.object.err.InitialPurchaseErrObj;
import com.pachia.comon.object.err.RetryPaymentErrObj;
import com.pachia.comon.object.err.VerifyPurchaseErrObj;
import com.pachia.comon.object.request.InitalizePuchaseRequestObj;
import com.pachia.comon.object.request.VerifyPurchaseRequestObj;
import com.pachia.comon.object.response.InitialPurchaseResponseObj;
import com.pachia.comon.object.response.ItemPayResponseObj;
import com.pachia.comon.object.response.RetryPaymentResponseObj;
import com.pachia.comon.object.response.VerifyPurchaseResponseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.tracking.CheShaoTracking;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentNativeFragment extends BaseDialogFragment implements PaymentAdapter.OnClickItemPaymentListener, BaseView {
    private static final String TAG = PaymentNativeFragment.class.getSimpleName();
    Activity mActivity;
    String account_id;
    private RecyclerView gridView;
    private View viewConnectionLost;
    ItemPayObj itemPayObj;
    private String state = "";
    List<SkuDetails> skuDetails;
    int current;

    TextView tv_title;

    PaymentPresenterImpl paymentPresenter;
    PaymentAdapter paymentAdapter;
    com.android.billingclient.api.Purchase purchase;
    PurchaseHistoryObj currentPurchase;
    InitalizePuchaseObj initalizePuchaseObj;

    ArrayList<IPaymentPresenter> listPresenter;

    PaymentSuccess paymentSuccess;

    public static PaymentNativeFragment newInstance(String state, PaymentSuccess paymentSuccess) {
        PaymentNativeFragment frag = new PaymentNativeFragment();
        frag.setPaymentSuccess(state, paymentSuccess);
        return frag;
    }


    @Override
    public void onStart() {
        super.onStart();
        ChelShaoHelper.hideNotiFloatButton();
        Objects.requireNonNull(getDialog().getWindow()).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0.8f);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.payment_dialog_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogTheme);
        initIAP();
        account_id = GameConfigs.getInstance().getUser().getAccount().getAccountId();
        listPresenter = new ArrayList<>();
        skuDetails = new ArrayList<>();
        current = -1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CheShaoTracking.getInstance().trackPaymentScreenOpened();
        initView(view);
        paymentPresenter = new PaymentPresenterImpl(this);
        paymentPresenter.getItemList();
    }


    @Override
    public void onResume() {
        super.onResume();
        checkPurchaseHistory();
    }

    private void initView(View view) {
        ImageButton btn_close;
        btn_close = (ImageButton) view.findViewById(R.id.btn_close_pay);
        gridView = (RecyclerView) view.findViewById(R.id.grid_view_pay);
        viewConnectionLost = view.findViewById(R.id.layout_connection_lost);
        Button btnReload = (Button) view.findViewById(R.id.btn_reload);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(mActivity.getResources().getString(R.string.txt_title_payment));

        viewConnectionLost.setVisibility(View.GONE);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentPresenter.getItemList();
            }
        });

    }

    private void initGridView(int col) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, col);
        if (itemPayObj != null) {
            paymentAdapter = new PaymentAdapter(mActivity, itemPayObj, this);
            gridView.setLayoutManager(gridLayoutManager);
            gridView.setAdapter(paymentAdapter);
        }
    }

    @Override
    public void onClickItem(View view, ItemPayObj.Item item) {

        Log.d(TAG, "onPaymentStartIAP clicked: " + item.getProductId());

        if (item.getConfigItem() != null && "webview".equals(item.getConfigItem().getType())) {
            showLocalPurchaseFragment(item);
        } else {
            String character_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");
            String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
            String invoice_no = DeviceUtils.getOSInfo().replace(" ", "") + System.currentTimeMillis();
            if (state == null || "".equals(state)) {
                state = "";
            }

            InitalizePuchaseRequestObj obj = new InitalizePuchaseRequestObj();
            obj.setRequest(PrefManager.getAppKey(mActivity));
            obj.setDevice_id(DeviceUtils.getUniqueDeviceID(mActivity));
            obj.setMethod("2");
            obj.setCharacter_name("");
            obj.setCharacter_id(character_id);
            obj.setArea_id(area_id);
            obj.setArea_name("");
            obj.setItem_no(item.getItemNo());
            obj.setProduct_id(item.getProductId());
            obj.setPayload(state);
            obj.setInvoice_no(invoice_no);
            obj.setApp_version(Utils.getGameVersion(mActivity));
            obj.setSdk_version(Utils.getSDKVersion(mActivity));
            obj.setDevice_name(DeviceUtils.getDevice());
            obj.setDevice_os(DeviceUtils.getOSInfo());
            obj.setNetwork(Utils.getNetwork(mActivity));
            obj.setAdvertising_id(DeviceUtils.getAdvertisingID(mActivity));
            obj.setAppsflyer_id(DeviceUtils.getAppsflyerUID(mActivity));
            obj.setLocale(PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.APP_LANG, "en"));
            obj.setResolution(DeviceUtils.getResolution(mActivity));

            if (paymentPresenter != null) paymentPresenter.initialPurchase(obj);
        }

    }

    private void showLocalPurchaseFragment(ItemPayObj.Item item) {
        PaymentWebviewFragment paymentWebviewFragment = PaymentWebviewFragment.newInstance(item, new PaymentWebviewFragment.LocalPurchaseListener() {
            @Override
            public void localPurchaseSuccess(VerifyPurchaseObj verifyPurchaseObj) {
                paymentSuccess.onLocalPaymentSuccess(verifyPurchaseObj);
                dismiss();
            }

            @Override
            public void localPurchaseFailed() {
                DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.err_and_try_again_late));
                dismiss();
            }
        });

        if (paymentWebviewFragment.getDialog() != null && paymentWebviewFragment.getDialog().isShowing())
            return;
        paymentWebviewFragment.show(mActivity.getFragmentManager(), PaymentNativeFragment.class.getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ChelShaoHelper.showNotiFloatButton(GameConfigs.getInstance().getSdkConfig().getEx());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CmdPaymentV3.getInstance().billingEndConnection();
    }

    private void initIAP() {
        CmdPaymentV3.getInstance().initIABv3(getActivity(), new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                int responseCode = billingResult.getResponseCode();
                String debugMessage = billingResult.getDebugMessage();
                Log.d(TAG, "onBillingSetupFinished: " + responseCode + " " + debugMessage);

                CmdPaymentV3.getInstance().queryHistory(new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
                        if(list==null){
                            Log.d(TAG, "PurchaseHistoryRecord is null ");
                        } else if (list.size() == 0) {
                            Log.d(TAG, "PurchaseHistoryRecord is empty ");
                        } else if (list.size() == 1) {
                            retryGooglePayment(list.get(0));
                            Log.d(TAG, "Item  : " + list.get(0).getPurchaseTime());
                        } else {
                            Log.d(TAG, "Item 1 : " + list.get(0).getPurchaseTime());
                            Log.d(TAG, "Item 2 : " + list.get(1).getPurchaseTime());
                            if (list.get(0).getPurchaseTime() > list.get(1).getPurchaseTime()) {
                                retryGooglePayment(list.get(0));
                                Log.d(TAG, "Purchase Item 1  ");
                            } else {
                                retryGooglePayment(list.get(1));
                                Log.d(TAG, "Purchase Item 2 ");
                            }
                        }
                    }
                });
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.d(TAG, "onBillingServiceDisconnected: ");
            }
        }, new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                int responseCode = billingResult.getResponseCode();
                String debugMessage = billingResult.getDebugMessage();
                Log.d(TAG, "onPurchasesUpdated:  " + responseCode + " " + debugMessage);
                switch (responseCode) {
                    case BillingClient.BillingResponseCode.OK:
                        processPurchases(list);
                        break;
                    case BillingClient.BillingResponseCode.USER_CANCELED:
                        Log.i(TAG, "onPurchasesUpdated: User canceled the purchase");
                        skuDetails.clear();

                        break;
                    case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                        Log.i(TAG, "onPurchasesUpdated: This item is unavailable");

                        break;
                    case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                        Log.i(TAG, "onPurchasesUpdated: The user already owns this item");

                        break;
                    case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                        Log.e(TAG, "onPurchasesUpdated: Developer error means that Google Play " +
                                "does not recognize the configuration. If you are just getting started, " +
                                "make sure you have configured the application correctly in the " +
                                "Google Play Console. The SKU product ID must match and the APK you " +
                                "are using must be signed with release keys."
                        );
                        break;
                    default:
                        Log.i(TAG, "onPurchasesUpdated: " + debugMessage);

                }
            }
        });
    }

    private final SkuDetailsResponseListener skuDetailsResponseListener = new SkuDetailsResponseListener() {
        @Override
        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
            int responseCode = billingResult.getResponseCode();
            String debugMessage = billingResult.getDebugMessage();
            switch (responseCode) {
                case BillingClient.BillingResponseCode.OK:
                    Log.d(TAG, "onSkuDetailsResponse: " + responseCode);
                    if (list != null && list.size() > 0) {
                        skuDetails = list;
                        CmdPaymentV3.getInstance().launchBillingFlow(getActivity(), skuDetails.get(skuDetails.size() - 1));
                    } else {
                        DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.product_id_is_incorrect));
                    }
                    break;
                case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                case BillingClient.BillingResponseCode.ERROR:
                    Log.e(TAG, "onSkuDetailsResponse: " + responseCode + " " + debugMessage);
                    break;
                case BillingClient.BillingResponseCode.USER_CANCELED:
                    Log.i(TAG, "onSkuDetailsResponse: " + responseCode + " " + debugMessage);
                    break;
                // These response codes are not expected.
                case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                default:
                    Log.wtf(TAG, "onSkuDetailsResponse: " + responseCode + " " + debugMessage);
            }
        }
    };

    private void processPurchases(List<com.android.billingclient.api.Purchase> purchasesList) {
        if (purchasesList != null) {
            Log.d(TAG, "processPurchases: " + purchasesList.size() + " purchase(s)");
            for (com.android.billingclient.api.Purchase purchase : purchasesList) {
                handlePurchase(purchase);
            }

        } else {
            Log.d(TAG, "processPurchases: with no purchases");
            Log.d(TAG, "onPurchasesUpdated: null purchase list");
        }

    }

    private void handlePurchase(com.android.billingclient.api.Purchase purchase) {
/*      UNSPECIFIED_STATE = 0;
        PURCHASED = 1;
        PENDING = 2;
*/
        Log.i(TAG, "handlePurchase: " + purchase.getPurchaseState());

        if (purchase.getPurchaseState() == com.android.billingclient.api.Purchase.PurchaseState.PURCHASED) {
            Log.i(TAG, "PURCHASE IS PURCHASED ");
            this.purchase = purchase;
            CmdPaymentV3.getInstance().consumeAsyncPurchase(purchase, new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                    skuDetails.clear();
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        currentPurchase = new PurchaseHistoryObj();
                        String character_id = PrefManager.getString(mActivity, Constants.ROLE_ID, "");
                        String area_id = PrefManager.getString(mActivity, Constants.AREA_ID, "");
                        currentPurchase.setArea_id(area_id);
                        currentPurchase.setCharactor_id(character_id);
                        currentPurchase.setOrder_no(initalizePuchaseObj.getOrder_no());
                        try {
                            JSONObject joReceipt = new JSONObject(purchase.getOriginalJson());
                            VerifyPurchaseRequestObj.Receipt rec = new VerifyPurchaseRequestObj.Receipt();
                            rec.setData(joReceipt.toString());
                            rec.setItemType("inapp");
                            rec.setSignature(purchase.getSignature());
                            currentPurchase.setReceipt(rec);
                        } catch (Exception e) {
                            LogUtils.e("Err Purchase", e.getMessage());
                        }
                        currentPurchase.setIs_send(false);
                        currentPurchase.setAccount_id(account_id);
                        LogUtils.d("RECEIPT 1", purchase.getOriginalJson());
                        for (IPaymentPresenter p : listPresenter) {
                            if (p != null)
                                p.cancelRequest();
                        }

                        PrefManager.saveUsePurchaseHistory(mActivity, account_id, currentPurchase);
                        PurchaseUtils.saveLastPurchaseGGSuccess(mActivity, account_id, purchase.getPurchaseToken());
                        Log.i(TAG, "Order no current purchase: " + currentPurchase.getOrder_no());
                        Log.i(TAG, "Save purchase token: " + purchase.getPurchaseToken());

                        if (paymentSuccess != null) {
                            dismiss();
                            paymentSuccess.onIapPaymentSuccess(currentPurchase);
//                             ToastUtils.showShortToast(mActivity, "Chưa verify giao dịch!");  //test retry
                        }
                    }
                }
            });


        } else if (purchase.getPurchaseState() == com.android.billingclient.api.Purchase.PurchaseState.PENDING) {
            Log.i(TAG, "PURCHASE IS PENDING ");
        }
    }

    @Override
    public void showProgress(String message) {
        Utils.showLoading(mActivity, true);
    }

    @Override
    public void hideProgress() {
        Utils.showLoading(mActivity, false);
    }

    @Override
    public void success(Object x) {
        if (x instanceof ItemPayResponseObj) {
            ItemPayResponseObj itemPayResponseObj = (ItemPayResponseObj) x;
            String message=  ((ItemPayResponseObj) x).getMessage();
            Log.d(TAG, "success response: " + message);
            if (itemPayResponseObj.getStatus() == 0 && itemPayResponseObj.getData() != null) {
                int column;
                CheShaoTracking.getInstance().trackPaymentLoadScreenSuccess();
                if (itemPayResponseObj.getData().getConfig() != null && itemPayResponseObj.getData().getConfig().getColumns() != null) {
                    column = itemPayResponseObj.getData().getConfig().getColumns();
                } else column = 3;
                itemPayObj = itemPayResponseObj.getData();
                if(itemPayObj.getItems().size()==0) {
                    DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.list_item_is_empty));
                }
                else {
                    initGridView(column);
                }
            } else if (itemPayResponseObj.getCode() == 1) {
                DialogUtils.showErrorDialog(mActivity, message);
                dismiss();
            }
        } else if (x instanceof InitialPurchaseResponseObj) {
            InitialPurchaseResponseObj initialPurchaseResponseObj = (InitialPurchaseResponseObj) x;
            Log.d(TAG, "InitialPurchaseResponseObj: " + ((InitialPurchaseResponseObj) x).getMessage());

            if (initialPurchaseResponseObj.getStatus() == 0) {
                initalizePuchaseObj = initialPurchaseResponseObj.getData();
                Log.d(TAG, "onPaymentStartIAP response: " + initalizePuchaseObj.getProduct_id());
                Log.d(TAG, "onPaymentStartIAP response: " + initalizePuchaseObj.getOrder_no());

                if (TextUtils.isEmpty(initalizePuchaseObj.getOrder_no())) {
                    DialogUtils.showErrorDialog(mActivity, ((InitialPurchaseResponseObj) x).getMessage());
                    return;
                }
                CmdPaymentV3.getInstance().querySkuDetails(initalizePuchaseObj.getProduct_id(), skuDetailsResponseListener);
            }
        }
    }

    @Override
    public void error(Object o) {
        if (o instanceof GetListPurchaseErrObj) {
            DialogUtils.showErrorDialog(mActivity, ((GetListPurchaseErrObj) o).getMessage());
            dismiss();
            CheShaoTracking.getInstance().trackPaymentLoadScreenFail(((GetListPurchaseErrObj) o).getStatus() + "", ((GetListPurchaseErrObj) o).getMessage());
        } else if (o instanceof InitialPurchaseErrObj) {
            DialogUtils.showErrorDialog(mActivity, ((InitialPurchaseErrObj) o).getMessage());
        } else if (o instanceof VerifyPurchaseErrObj) {
            DialogUtils.showErrorDialog(mActivity, ((VerifyPurchaseErrObj) o).getMessage());
        }
    }

    public interface PaymentSuccess {
        void onIapPaymentSuccess(PurchaseHistoryObj obj);

        void onLocalPaymentSuccess(VerifyPurchaseObj verifyPurchaseObj);
    }

    public void setPaymentSuccess(String state, PaymentSuccess paymentSuccess) {
        this.state = state;
        this.paymentSuccess = paymentSuccess;
    }

    public void checkPurchaseHistory() {
        current += 1;
        ListPurchaseHistoryObj listPurchaseHistoryObj = PrefManager.getHistoryPurchase(mActivity, account_id);
        if (listPurchaseHistoryObj.getData() == null || listPurchaseHistoryObj.getData().size() == 0 || listPurchaseHistoryObj.getData().size() <= current && current >= 0)
            return;
        PurchaseHistoryObj obj = listPurchaseHistoryObj.getData().get(current);
        sendHistory(obj);
    }

    public void sendHistory(PurchaseHistoryObj obj) {
        IPaymentPresenter presenter = new PaymentPresenterImpl(new BaseView() {
            @Override
            public void showProgress(String message) {

            }

            @Override
            public void hideProgress() {

            }

            @Override
            public void success(Object x) {
                if (x instanceof VerifyPurchaseResponseObj) {
                    VerifyPurchaseResponseObj responseObj = (VerifyPurchaseResponseObj) x;
                    int status = responseObj.getData().getStatus();
                    if (status == 3 || status == 2 || status == 17)
                        CheShaoTracking.getInstance().trackPaymentVerifySuccess(responseObj.getData());
                    else
                        CheShaoTracking.getInstance().trackPaymentVerifyFail(obj.getOrder_no(), "code", String.valueOf(status), responseObj.getData().getDescription());
                    obj.setAccount_id(account_id);
                    PurchaseUtils.removeSuccessPurchase(obj, mActivity);
                    current -= 1;
                    checkPurchaseHistory();
                    LogUtils.e(TAG, "Verify Success - " + obj.getOrder_no());
                }
            }

            @Override
            public void error(Object o) {
                if (o instanceof VerifyPurchaseErrObj) {
                    VerifyPurchaseErrObj err = (VerifyPurchaseErrObj) o;
                    LogUtils.e(TAG, "Verify failed - " + obj.getOrder_no() + " :" + err.getMessage());
                    checkPurchaseHistory();
                }
                if (o instanceof BaseObj) {
                    BaseObj err = (BaseObj) o;
                    CheShaoTracking.getInstance().trackPaymentVerifyFail(obj.getOrder_no(), "http", String.valueOf(err.getStatus()), err.getMessage());
                }

            }
        });
        CheShaoTracking.getInstance().trackPaymentBeforVerify(obj.getOrder_no());
        listPresenter.add(presenter);
        onVerifyPurchase(presenter, obj.getReceipt(), obj.getOrder_no());
    }

    public void onVerifyPurchase(IPaymentPresenter presenter, VerifyPurchaseRequestObj.Receipt receipt, String order_no) {
        VerifyPurchaseRequestObj obj = new VerifyPurchaseRequestObj();
        obj.setOrder_no(order_no);
        obj.setMethod("2");
        obj.setReceipt(receipt);
        obj.setApp_version(Utils.getGameVersion(mActivity));
        obj.setSdk_version(Utils.getSDKVersion(mActivity));
        obj.setDevice_name(DeviceUtils.getDevice());
        obj.setDevice_os(DeviceUtils.getOSInfo());
        obj.setResolution(DeviceUtils.getResolution(mActivity));
        obj.setNetwork(Utils.getNetwork(mActivity));
        obj.setAdvertising_id(DeviceUtils.getAdvertisingID(mActivity));
        obj.setAppsflyer_id(DeviceUtils.getAppsflyerUID(mActivity));
        obj.setLocale(GameConfigs.getInstance().getLang());
        if (presenter != null)
            presenter.verifyPurchase(obj);
    }


    public void retryGooglePayment(PurchaseHistoryRecord pr) {
        if (PurchaseUtils.isPurchaseTokenRequested(mActivity, pr.getPurchaseToken())) {
            LogUtils.d(TAG, "check History : tất cả giao dịch đã được xử lý");
        } else {
            LogUtils.d(TAG, "Retry giao dịch với purchase token: " +pr.getPurchaseToken());
            IPaymentPresenter presenter = new PaymentPresenterImpl(new BaseView() {
                @Override
                public void showProgress(String message) {

                }

                @Override
                public void hideProgress() {

                }

                @Override
                public void success(Object x) {
                    if (x instanceof RetryPaymentResponseObj) {
                        RetryPaymentResponseObj o = (RetryPaymentResponseObj) x;
                        LogUtils.d(TAG, "Retry Success: " + o.getMessage());
                        ToastUtils.showLengthInfoToast(mActivity, o.getMessage());
                        PurchaseUtils.markPurchaseToken(mActivity, pr.getPurchaseToken(), true);
                        LogUtils.d(TAG, "Removed purchase token: " +pr.getPurchaseToken());
                        if (o.getStatus() == 1 || o.getStatus() == 2028 || o.getStatus() == 2027) {
                            CheShaoTracking.getInstance().trackRetryPurchaseFail(String.valueOf(o.getStatus()), o.getMessage());
                        }
                    }
                }

                @Override
                public void error(Object o) {
                    if (o instanceof RetryPaymentErrObj) {
                        RetryPaymentErrObj err = (RetryPaymentErrObj) o;
                        LogUtils.e(TAG, "Retry failed: " + err.getMessage());
                        if (err.getStatus() == 1 || err.getStatus()==400 || err.getStatus() == 2028 || err.getStatus() == 2027) {
                            PurchaseUtils.markPurchaseToken(mActivity, pr.getPurchaseToken(), true);
                            LogUtils.d(TAG, "Removed purchase token: " +pr.getPurchaseToken());
                        }
                        CheShaoTracking.getInstance().trackRetryPurchaseFail(String.valueOf(err.getStatus()), err.getMessage());
                    }
                }
            });
            String appKey = GameConfigs.getInstance().getAppKey();
            int userId = GameConfigs.getInstance().getUser().getId();
            String role_id = PrefManager.getString(mActivity, Constants.ROLE_ID, "");
            String area_id = PrefManager.getString(mActivity, Constants.AREA_ID, "");
            presenter.retryPurchase(appKey, String.valueOf(userId), role_id, area_id, mActivity.getPackageName(), pr.getPurchaseToken(), pr.getSku());
        }
    }
}
