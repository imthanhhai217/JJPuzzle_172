package com.pachia.comon.tracking;

import com.appsflyer.AppsFlyerTrackingRequestListener;

public abstract class BaseTrackingListener<T extends String> implements AppsFlyerTrackingRequestListener {

    public abstract void onSuccess();

    public abstract void onError(T object);
}
