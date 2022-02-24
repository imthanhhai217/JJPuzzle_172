package com.pachia.comon.presenter;

import java.util.Map;

/**
 * Created by dungnv
 */
public class BaseParameter {
    public static final String TAG = BaseParameter.class.getSimpleName();

    /*public static HashMap<String, String> getBaseParameter() {
        HashMap<String, String> params = new HashMap<String, String>();
        UserObj userObj = PrefManager.getUser(Apps.getInstance());
        if (userObj != null) {
            params.put("update_by", userObj.getData().getId());
        } else {
            params.put("update_by", ""); // ko co id
        }
        params.put("language", PrefManager.getLanguage(Apps.getInstance())); // language
        return params;
    }

    public static HashMap<String, String> getBaseParameter(boolean needId) {
        HashMap<String, String> params = new HashMap<String, String>();
        UserObj userObj = PrefManager.getUser(Apps.getInstance());
        if (userObj != null && needId) {
            params.put("update_by", userObj.getData().getId());
        }
        params.put("language", PrefManager.getLanguage(Apps.getInstance())); // language
        return params;
    }*/

    /**
     * change key process id = id correct of api
     *
     * @param id
     * @param params
     * @return
     */
    public static Map<String, String> updateKeyParam(String id, Map<String, String> params, String oldId) {
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue().contains(oldId)) {
                    entry.setValue(id);
                }
            }
        }
        return params;
    }
}
