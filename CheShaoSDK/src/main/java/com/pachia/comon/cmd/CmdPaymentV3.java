package com.pachia.comon.cmd;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.pachia.comon.api.ErrorCode;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.listener.IPaymentListener;
import com.pachia.comon.object.BaseObj;
import com.pachia.comon.object.PurchaseHistoryObj;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.object.err.VerifyPurchaseErrObj;
import com.pachia.comon.object.request.VerifyPurchaseRequestObj;
import com.pachia.comon.object.response.VerifyPurchaseResponseObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.tracking.CheShaoTracking;
import com.pachia.comon.utils.DeviceUtils;
import com.pachia.comon.utils.DialogUtils;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.PurchaseUtils;
import com.pachia.comon.utils.Utils;
import com.pachia.ui.payment.IPaymentPresenter;
import com.pachia.ui.payment.PaymentNativeFragment;
import com.pachia.ui.payment.PaymentPresenterImpl;
import com.quby.R;

import java.util.ArrayList;
import java.util.List;

public class CmdPaymentV3 {

    private static final int TYPE_UNKNOWN = 0;
    private static final int TYPE_IAB = 1;
    private static final int TYPE_PAYPAL = 2;

    private static final String TAG = CmdPaymentV3.class.getSimpleName();
    // iab v3
    private BillingClient billingClient;
    Context context;
    private static CmdPaymentV3 INSTANCE;

    public static CmdPaymentV3 getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CmdPaymentV3();
        }
        return INSTANCE;
    }

    private CmdPaymentV3() {
    }

    public void initIABv3(Context context, BillingClientStateListener billingClientStateListener, PurchasesUpdatedListener purchasesUpdatedListener) {

        if (context != null) {
            this.context = context;
        }

        if (billingClient == null) {
            billingClient = BillingClient.newBuilder(context)
                    .setListener(purchasesUpdatedListener)
                    .enablePendingPurchases()
                    .build();
        } else {
            Log.d(TAG, "BillingClient is not null");
        }

        if (!billingClient.isReady()) {
            Log.d(TAG, "BillingClient: Start connection...");
            billingClient.startConnection(billingClientStateListener);
        } else {

            Log.d(TAG, "BillingClient: Started connection");
        }
    }


    public void querySkuDetails(String productID, SkuDetailsResponseListener skuDetailsResponseListener) {
        Log.d(TAG, "querySkuDetails");

        List<String> skus = new ArrayList<>();
        skus.add(productID);

        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setType(BillingClient.SkuType.INAPP)
                .setSkusList(skus)
                .build();

        Log.i(TAG, "querySkuDetailsAsync");
        billingClient.querySkuDetailsAsync(params, skuDetailsResponseListener);
    }

    public int launchBillingFlow(Activity activity, SkuDetails skuDetail) {

        if (!billingClient.isReady()) {
            Log.e(TAG, "launchBillingFlow: BillingClient is not ready");
        }
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetail)
                .build();

        BillingResult billingResult = billingClient.launchBillingFlow(activity, billingFlowParams);
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        Log.d(TAG, "launchBillingFlow: BillingResponse " + responseCode + " " + debugMessage);
        return responseCode;
    }


    // Consumable
    public void consumeAsyncPurchase(Purchase purchase, ConsumeResponseListener consumeResponseListener) {
        ConsumeParams consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        billingClient.consumeAsync(consumeParams, consumeResponseListener);
    }

    public void billingEndConnection() {
        if (billingClient.isReady()) {
            Log.d(TAG, "BillingClient can only be used once -- closing connection");
            // BillingClient can only be used once.
            // After calling endConnection(), we must create a new BillingClient.
            billingClient.endConnection();
            billingClient=null;

        }
    }

    public void verifyPayment(Activity mActivity, PurchaseHistoryObj historyObj, IPaymentListener iPaymentListener) {
        VerifyPurchaseRequestObj obj = new VerifyPurchaseRequestObj();
        obj.setOrder_no(historyObj.getOrder_no());
        obj.setMethod("2");
        obj.setReceipt(historyObj.getReceipt());
        obj.setApp_version(Utils.getGameVersion(mActivity));
        obj.setSdk_version(Utils.getSDKVersion(mActivity));
        obj.setDevice_name(DeviceUtils.getDevice());
        obj.setDevice_os(DeviceUtils.getOSInfo());
        obj.setResolution(DeviceUtils.getResolution(mActivity));
        obj.setNetwork(Utils.getNetwork(mActivity));
        obj.setAdvertising_id(DeviceUtils.getAdvertisingID(mActivity));
        obj.setAppsflyer_id(DeviceUtils.getAppsflyerUID(mActivity));
        obj.setLocale(GameConfigs.getInstance().getLang());
        IPaymentPresenter presenter = new PaymentPresenterImpl(new BaseView() {
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
                if (x instanceof VerifyPurchaseResponseObj) {
                    VerifyPurchaseResponseObj responseObj = (VerifyPurchaseResponseObj) x;
                    String lastPurchaseToken = PurchaseUtils.getLastPurchaseGGSuccess(mActivity, historyObj.getAccount_id());
                    PurchaseUtils.markPurchaseToken(mActivity, lastPurchaseToken, true);
                    LogUtils.d(PaymentNativeFragment.class.getSimpleName(), "Removed purchase token: " + lastPurchaseToken);
                    if (responseObj.getData().getStatus() == 3 || responseObj.getData().getStatus() == 2 || responseObj.getData().getStatus() == 17) {
                        LogUtils.e(TAG, "Verify Success - " + historyObj.getOrder_no());
                        PurchaseUtils.removeSuccessPurchase(historyObj, mActivity);
                        CheShaoTracking.getInstance().trackPaymentVerifySuccess(responseObj.getData());
                        if (iPaymentListener != null)
                            iPaymentListener.onPaymentSuccess(responseObj.getData());
                    } else {
                        CheShaoTracking.getInstance().trackPaymentVerifyFail(obj.getOrder_no(), "code", String.valueOf(responseObj.getData().getStatus()), responseObj.getData().getDescription());
                        DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.err_verify_purchase), new DialogUtils.DlgCloseListener() {
                            @Override
                            public void onClose() {
                            }
                        });
                    }
                }
            }

            @Override
            public void error(Object o) {
                BaseObj apiErrorObj = (BaseObj) o;
                CheShaoTracking.getInstance().trackPaymentVerifyFail(obj.getOrder_no(), "http", String.valueOf(apiErrorObj.getStatus()), apiErrorObj.getMessage());
                if (apiErrorObj.getStatus() == ErrorCode.NO_INTERNET) {
                    DialogUtils.showErrorDialog(mActivity, mActivity.getString(R.string.error_network));
                    return;
                }
                if (o instanceof VerifyPurchaseErrObj) {
                    VerifyPurchaseErrObj obj = (VerifyPurchaseErrObj) o;
                    if (obj.getStatus() == Constants.USER_ERR_CODE.INVALID_TOKEN) {
                        DialogUtils.showExpireDialog(mActivity);
                    } else {
                        int count_time = mActivity.getIntent().getIntExtra("count_time", 0);
                        if (count_time <= 2)
                            DialogUtils.showPaymentRetryDialog(mActivity, obj.getMessage(), new DialogUtils.Listener() {
                                @Override
                                public void onRetry() {
                                    mActivity.getIntent().putExtra("count_time", count_time + 1);
                                    verifyPayment(mActivity, historyObj, iPaymentListener);
                                }
                            });
                        else
                            DialogUtils.showErrorDialog(mActivity, obj.getMessage(), new DialogUtils.DlgCloseListener() {
                                @Override
                                public void onClose() {
                                }
                            });
                    }
                }
            }
        });
        CheShaoTracking.getInstance().trackPaymentBeforVerify(historyObj.getOrder_no());
        presenter.verifyPurchase(obj);
    }


    public void queryHistory(PurchaseHistoryResponseListener listener) {
        if (billingClient != null && listener != null)
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, listener);
    }
}