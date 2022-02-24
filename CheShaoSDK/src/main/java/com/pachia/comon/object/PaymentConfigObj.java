package com.pachia.comon.object;

import com.google.gson.annotations.SerializedName;


/**
 * Created by dungnv
 */

public class PaymentConfigObj extends BaseObj {

    @SerializedName("scroll")
    private int scroll;

    @SerializedName("row")
    private int row;

    @SerializedName("col")
    private int col;

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
