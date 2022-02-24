package com.pachia.ui.payment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pachia.comon.game.BaseDialogWebPaymentFragment;
import com.pachia.comon.js.JsBase;
import com.pachia.comon.js.JsPayment;
import com.pachia.comon.object.ItemPayObj;
import com.pachia.comon.object.VerifyPurchaseObj;
import com.quby.R;
import com.pachia.comon.listener.IWebViewClientListener;
import com.pachia.comon.tracking.CheShaoTracking;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentWebviewFragment extends BaseDialogWebPaymentFragment implements IWebViewClientListener {

    LocalPurchaseListener iPaymentListener;

    public static PaymentWebviewFragment newInstance(ItemPayObj.Item item, LocalPurchaseListener iPaymentListener) {
        PaymentWebviewFragment frag = new PaymentWebviewFragment();
        Bundle args = new Bundle();
        String url = item.getConfigItem().getUrl();
        Log.d(PaymentWebviewFragment.class.getSimpleName(), "url: " + url);
        args.putString(URL_WEBVIEW, url);
        frag.setArguments(args);
        frag.setPaymentListener(iPaymentListener);
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleForm(mActivity.getResources().getString(R.string.txt_title_payment));
    }

    @Override
    protected JsBase getJsHandler() {
        return new JsPayment(new JsPayment.Listener() {
            @Override
            public void onPaymentStartIAPV3(String param) {

            }

            @Override
            public void onLocalPayment(String param) {
                Log.d(PaymentWebviewFragment.class.getSimpleName(), "Local purchase param: " + param);

                try {
                    JSONObject order = new JSONObject(param);
                    String order_no = "";
                    if (order.has("order_no")) {
                        order_no = order.getString("order_no");
                    }

                    int status = 0;
                    if (order.has("status")) {
                        status = order.getInt("status");
                    }
                    String created_at = "";
                    if (order.has("created_at")) {
                        created_at = order.getString("created_at");
                    }
                    int user_id = 0;
                    if (order.has("user_id")) {
                        user_id = order.getInt("user_id");
                    }
                    int item_value = 0;
                    if (order.has("item_value")) {
                        item_value = order.getInt("item_value");
                    }

                    String description = "";
                    if (order.has("description")) {
                        description = order.getString("description");
                    }
                    String character_id = "";
                    if (order.has("character_id")) {
                        character_id = order.getString("character_id");
                    }

                    long platform_price = 0;
                    if (order.has("exchange_value")) {
                        platform_price = order.getLong("exchange_value");
                    }
                    String payload = "";
                    if (order.has("payload")) {
                        payload = order.getString("payload");
                    }
                    boolean is_sandbox = false;
                    if (order.has("sandbox")) {
                        is_sandbox = order.getBoolean("is_sandbox");
                    }

                    String area_id = "";
                    if (order.has("area_id")) {
                        area_id = order.getString("area_id");
                    }

                    VerifyPurchaseObj verifyPurchaseObj = new VerifyPurchaseObj();
                    verifyPurchaseObj.setOrder_no(order_no);
                    verifyPurchaseObj.setStatus(status);
                    verifyPurchaseObj.setCreated_at(created_at);
                    verifyPurchaseObj.setUser_id(user_id);
                    verifyPurchaseObj.setItem_value(item_value);
                    verifyPurchaseObj.setDescription(description);
                    verifyPurchaseObj.setCharacter_id(character_id);
                    verifyPurchaseObj.setPlatform_price(platform_price);
                    verifyPurchaseObj.setPayload(payload);
                    verifyPurchaseObj.setIs_sandbox(is_sandbox);
                    verifyPurchaseObj.setArea_id(area_id);

                    if (status == 3) {
                        if (iPaymentListener != null) {
                            CheShaoTracking.getInstance().trackPaymentVerifySuccess(verifyPurchaseObj);
                            iPaymentListener.localPurchaseSuccess(verifyPurchaseObj);
                            dismiss();
                        }
                    } else {
                        if (iPaymentListener != null) {
                            iPaymentListener.localPurchaseFailed();
                            CheShaoTracking.getInstance().trackLocalPaymentVerifyFail(verifyPurchaseObj);
                            dismiss();
                        }
                    }
                } catch (JSONException e) {
                    Log.e(PaymentWebviewFragment.class.getSimpleName(), "onLocalPayment: " + e.getMessage());
                }


            }

            @Override
            public void onCloseWindow() {

            }

            @Override
            public void onOpenWindow() {

            }

            @Override
            public void onBackToWindow() {

            }
        });
    }

    @Override
    protected IWebViewClientListener getWebListener() {
        return this;
    }

    public void setPaymentListener(LocalPurchaseListener iPaymentListener) {
        this.iPaymentListener = iPaymentListener;
    }

    @Override
    public void shouldOverrideUrlLoading(WebView view, String url) {

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {
    }

    public interface LocalPurchaseListener {
        void localPurchaseSuccess(VerifyPurchaseObj verifyPurchaseObj);

        void localPurchaseFailed();
    }

}
