package com.pachia.ui.payment;

import com.pachia.comon.object.request.InitalizePuchaseRequestObj;
import com.pachia.comon.object.request.VerifyPurchaseRequestObj;
import com.pachia.comon.presenter.BaseView;
import com.pachia.comon.presenter.InteractorCallback;

public class PaymentPresenterImpl implements IPaymentPresenter {

    private IPaymentInteractor mInteractor;
    private BaseView mBaseView;

    public PaymentPresenterImpl(BaseView mBaseView) {
        this.mBaseView = mBaseView;
        mInteractor = new PaymentInteractorImpl(mCallback);
    }

    private final InteractorCallback<Object> mCallback = new InteractorCallback<Object>() {
        @Override
        public void success(Object x) {
            if (mBaseView != null) {
                mBaseView.hideProgress();
                mBaseView.success(x);
            }
        }

        @Override
        public void error(Object o) {
            if (mBaseView != null) {
                mBaseView.hideProgress();
                mBaseView.error(o);
            }
        }
    };

    @Override
    public void cancelRequest(String... tags) {
        mInteractor.cancelRequest();
    }


    @Override
    public void getItemList() {
            mInteractor.cancelRequest();
            mInteractor.getItemList();
        if (mBaseView != null) {

            mBaseView.showProgress("");
        }
    }

    @Override
    public void verifyPurchase(VerifyPurchaseRequestObj obj) {
        mInteractor.cancelRequest();
        mInteractor.verifyPurchase(obj);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void initialPurchase(InitalizePuchaseRequestObj initalizePuchaseRequestObj) {
        mInteractor.cancelRequest();
        mInteractor.initialPurchase(initalizePuchaseRequestObj);
        if (mBaseView != null) {
            mBaseView.showProgress("");
        }
    }

    @Override
    public void retryPurchase(String appKey, String userId, String characterId, String areaId, String packageName, String purchaseToken, String productId) {
        mInteractor.cancelRequest();
        mInteractor.retryPurchase(appKey, userId, characterId, areaId, packageName, purchaseToken, productId);
    }


}
