package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class PaypalConfigObj extends BaseObj {

    @SerializedName("accept_credit_cards")
    private int accept_credit_cards;

    public int getAccept_credit_cards() {
        return accept_credit_cards;
    }

    public void setAccept_credit_cards(int accept_credit_cards) {
        this.accept_credit_cards = accept_credit_cards;
    }


    public boolean isAcceptScreditCards() {
        return getAccept_credit_cards() == 1 ? true : false;
    }

}
