package com.pachia.comon.listener;

import com.pachia.comon.object.MessInGameObj;

import java.util.ArrayList;

public interface IMesssageListener {
    void onSuccess(ArrayList<MessInGameObj> data);
}
