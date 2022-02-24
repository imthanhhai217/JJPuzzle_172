package com.pachia.comon.presenter;

/**
 * Created by dungnv
 */
public interface BaseResponse<E> {

    void success(E x);

//    void success(E x);

    /**
     * handle error
     *
     * @param e of request. sample json format error, http error (400, 404, ...)
     */
    void error(E e);
}
