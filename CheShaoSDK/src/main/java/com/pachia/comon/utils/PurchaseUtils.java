package com.pachia.comon.utils;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.pachia.comon.object.ListPurchaseHistoryObj;
import com.pachia.comon.object.PurchaseHistoryObj;
import com.pachia.comon.sharePref.PrefManager;

public class PurchaseUtils {

    public static void removeSuccessPurchase(PurchaseHistoryObj obj, Activity activity) {
        ListPurchaseHistoryObj listPurchaseHistoryObj = PrefManager.getHistoryPurchase(activity, obj.getAccount_id());
        for (int i = 0; i < listPurchaseHistoryObj.getData().size(); i++) {
            if (listPurchaseHistoryObj.getData().get(i).getOrder_no().equals(obj.getOrder_no())) {
                listPurchaseHistoryObj.getData().remove(i);
                break;
            }
        }
        PrefManager.saveUsePurchaseHistory(activity, obj.getAccount_id(), listPurchaseHistoryObj);
    }

    public static void saveLastPurchaseGGSuccess(@NonNull Activity activity, @NonNull String accountId, @NonNull String purchaseToken) {
        PrefManager.saveLastPurchaseGGSuccess(activity, accountId, purchaseToken);
    }

    public static String getLastPurchaseGGSuccess(@NonNull Activity activity, @NonNull String accountId) {
        return PrefManager.getLastPurchaseGGSuccess(activity, accountId);
    }

    public static void markPurchaseToken(@NonNull Activity activity, @NonNull String purchaseToken, boolean isRequest) {
        PrefManager.saveBoolean(activity, purchaseToken, isRequest);
    }
    public static boolean isPurchaseTokenRequested(@NonNull Activity activity, @NonNull String purchaseToken) {
        return PrefManager.getBoolean(activity, purchaseToken, false);
    }
}
