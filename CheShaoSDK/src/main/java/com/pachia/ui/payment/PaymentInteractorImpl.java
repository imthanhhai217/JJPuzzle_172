package com.pachia.ui.payment;


import com.pachia.comon.api.ApiUtils;
import com.pachia.comon.api.MyCallback;
import com.pachia.comon.api.request.PaymentRequest;
import com.pachia.comon.config.GameConfigs;
import com.pachia.comon.constants.Constants;
import com.pachia.comon.game.CheShaoSdk;
import com.pachia.comon.object.BaseObj;
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
import com.pachia.comon.presenter.InteractorCallback;
import com.pachia.comon.sharePref.PrefManager;
import com.pachia.comon.utils.LogUtils;
import com.pachia.comon.utils.Utils;

import retrofit2.Call;
import retrofit2.Response;

public class PaymentInteractorImpl implements IPaymentInteractor {
    private static final String TAG = PaymentInteractorImpl.class.getName();

    private InteractorCallback mCallback;

    Call<VerifyPurchaseResponseObj> mVerifyPurchaseResponseObj;
    Call<ItemPayResponseObj> itemPayResponseObjCall;
    Call<InitialPurchaseResponseObj> initialPurchaseResponseObjCall;
    Call<RetryPaymentResponseObj> mRetryPaymentResponseObj;

    public PaymentInteractorImpl(InteractorCallback mCallback) {
        this.mCallback = mCallback;
    }


    @Override
    public void verifyPurchase(VerifyPurchaseRequestObj obj) {
        PaymentRequest request = ApiUtils.getPaymentRequest();
        mVerifyPurchaseResponseObj = request.verifyPurchase(obj.getOrder_no(), obj);

        LogUtils.d("RECEIPT", obj.getReceipt().toString());
        mVerifyPurchaseResponseObj.enqueue(new MyCallback<VerifyPurchaseResponseObj>() {
            @Override
            public void onSuccess(Call<VerifyPurchaseResponseObj> call, Response<VerifyPurchaseResponseObj> response) {
                if (response.isSuccessful()) {
                    mCallback.success(response.body());
                }
            }

            @Override
            public void onError(Call<VerifyPurchaseResponseObj> call, Object object) {
                BaseObj obj = (BaseObj) object;
                VerifyPurchaseErrObj err = new VerifyPurchaseErrObj();
                err.setMessage(obj.getMessage());
                err.setStatus(obj.getStatus());
                mCallback.error(err);
            }
        });
    }

    @Override
    public void getItemList() {
        PaymentRequest request = ApiUtils.getPaymentRequest();
        String method = "2"; //GG PLAY
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        String appKey = PrefManager.getAppKey(CheShaoSdk.getInstance().getApplication());
        String sdkVersion = Utils.stringNormalize(Utils.getSDKVersion(CheShaoSdk.getInstance().getApplication()));
        String account_id = GameConfigs.getInstance().getUser().getAccount().getAccountId();
        String role_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.ROLE_ID, "");;
        String area_id = PrefManager.getString(CheShaoSdk.getInstance().getApplication(), Constants.AREA_ID, "");
        itemPayResponseObjCall = request.getListItem(method, appKey, appVer, sdkVersion, account_id, role_id, area_id);
        itemPayResponseObjCall.enqueue(new MyCallback<ItemPayResponseObj>() {
            @Override
            public void onSuccess(Call<ItemPayResponseObj> call, Response<ItemPayResponseObj> response) {
                if (response.isSuccessful()) {
                    mCallback.success(response.body());
                }
            }

            @Override
            public void onError(Call<ItemPayResponseObj> call, Object object) {
                BaseObj obj = (BaseObj) object;
                GetListPurchaseErrObj err = new GetListPurchaseErrObj();
                err.setMessage(obj.getMessage());
                err.setStatus(obj.getStatus());
                mCallback.error(err);
            }
        });

    }

    @Override
    public void initialPurchase(InitalizePuchaseRequestObj initialPurchaseRequestObj) {
        PaymentRequest request = ApiUtils.getPaymentRequest();
        initialPurchaseResponseObjCall = request.initialPurchase(initialPurchaseRequestObj);
        initialPurchaseResponseObjCall.enqueue(new MyCallback<InitialPurchaseResponseObj>() {
            @Override
            public void onSuccess(Call<InitialPurchaseResponseObj> call, Response<InitialPurchaseResponseObj> response) {
                if (response.isSuccessful()) {
                    mCallback.success(response.body());
                }
            }

            @Override
            public void onError(Call<InitialPurchaseResponseObj> call, Object object) {
                BaseObj obj = (BaseObj) object;
                InitialPurchaseErrObj err = new InitialPurchaseErrObj();
                err.setMessage(obj.getMessage());
                err.setStatus(obj.getStatus());
                mCallback.error(err);
            }
        });


    }

    @Override
    public void retryPurchase(String appKey, String userId, String characterId, String areaId, String packageName, String purchaseToken, String productId) {
        PaymentRequest request = ApiUtils.getPaymentRequest();
        String appVer = Utils.getGameVersion(CheShaoSdk.getInstance().getApplication());
        mRetryPaymentResponseObj = request.retryPayment(appKey, appVer, userId, characterId, areaId, packageName, purchaseToken, productId);
        mRetryPaymentResponseObj.enqueue(new MyCallback<RetryPaymentResponseObj>() {
            @Override
            public void onSuccess(Call<RetryPaymentResponseObj> call, Response<RetryPaymentResponseObj> response) {
                if (response.isSuccessful()) {
                    mCallback.success(response.body());
                }
            }

            @Override
            public void onError(Call<RetryPaymentResponseObj> call, Object object) {
                BaseObj obj = (BaseObj) object;
                RetryPaymentErrObj err = new RetryPaymentErrObj();
                err.setMessage(obj.getMessage());
                err.setStatus(obj.getStatus());
                mCallback.error(err);
            }
        });
    }

    @Override
    public void cancelRequest(String... tags) {
        if (mVerifyPurchaseResponseObj != null)
            mVerifyPurchaseResponseObj.cancel();
    }

}
