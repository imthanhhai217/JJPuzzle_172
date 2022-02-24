package com.pachia.ui.payment;

import com.pachia.comon.object.request.InitalizePuchaseRequestObj;
import com.pachia.comon.object.request.VerifyPurchaseRequestObj;
import com.pachia.comon.presenter.BasePresenter;

public interface IPaymentPresenter extends BasePresenter {
    void getItemList();
    void verifyPurchase(VerifyPurchaseRequestObj obj);
    void initialPurchase(InitalizePuchaseRequestObj initalizePuchaseRequestObj);
    void retryPurchase(String appKey, String userId, String characterId, String areaId, String packageName, String purchaseToken, String productId);
}
