package com.pachia.balat.demo.models;

import android.content.Context;

public class PieceModel extends androidx.appcompat.widget.AppCompatImageView {

    public boolean canMoveThis = true;
    private int x;
    private int y;
    private int w;
    private int h;

    public PieceModel(Context context) {
        super(context);
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isCanMoveThis() {
        return canMoveThis;
    }

    public void setCanMoveThis(boolean canMoveThis) {
        this.canMoveThis = canMoveThis;
    }
}