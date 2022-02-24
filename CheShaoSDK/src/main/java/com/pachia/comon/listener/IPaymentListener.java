package com.pachia.comon.listener;

import com.pachia.comon.object.VerifyPurchaseObj;

public interface IPaymentListener {
    void onPaymentSuccess(VerifyPurchaseObj obj);
}
