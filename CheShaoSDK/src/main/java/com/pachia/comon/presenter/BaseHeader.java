package com.pachia.comon.presenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dungnv
 */
public class BaseHeader {
    public static Map<String, String> getHeader() {
        HashMap<String, String> params = new HashMap<String, String>();

      /*  String token = PrefManager.getString(Apps.getInstance(), Constants.PrefKey.PREF_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            params.put("Authorization", "Bearer " + token);
        }*/
        return params;
    }
}
