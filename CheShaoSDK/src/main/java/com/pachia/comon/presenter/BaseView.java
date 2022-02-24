package com.pachia.comon.presenter;

/**
 * Created by dungnv
 */
public interface BaseView extends BaseResponse {
    /**
     * show progress view.
     */
    void showProgress(String message);

    /**
     * hide progress view.
     */
    void hideProgress();
}