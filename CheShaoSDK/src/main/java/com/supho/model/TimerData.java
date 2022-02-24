package com.supho.model;

/**
 * Created by Khai Tran on 5/26/2017.
 */
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.supho.utils.TimeUtils;
import com.google.gson.Gson;
import com.supho.utils.FloatButtonTimerHelper;

public class TimerData {
    public static final String TAG = TimerData.class.getSimpleName();

    private ArrayList<TimerObject> listTimerObject;

    public TimerData(){

    }
    public static TimerData parse(JSONArray timerObjs) throws JSONException {
        Log.d(TAG, "TimerData::parse: " + timerObjs);
        if(timerObjs == null){
            return new TimerData();
        }
        ArrayList<TimerObject> listTimer = new ArrayList<>();
        long now = Calendar.getInstance().getTimeInMillis();

        for (int i = 0; i < timerObjs.length(); i++){
            try {
                JSONObject timerObj = timerObjs.optJSONObject(i);
                long startTimeMils = TimeUtils.getMilisecondByTimestamp(timerObj.optLong("start"));
                long endTimeMilis = TimeUtils.getMilisecondByTimestamp(timerObj.optLong("end"));
                int id = timerObj.getInt("id");
                if(startTimeMils <= 0 || endTimeMilis <= 0){
                    Log.d(TAG, String.format("Time value <= 0. Skip!"));
                    continue;
                }

                if(endTimeMilis < now){
                    Log.d(TAG, String.format("EndTime(%s) < Now(%s) => Expired!",
                            endTimeMilis, now));
                    continue;
                }
                TimerObject timer = new TimerObject();
                timer.setId(id);
                timer.setStartTime(startTimeMils);
                timer.setEndTime(endTimeMilis);
                listTimer.add(timer);

            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        TimerData timerData = new TimerData();
        timerData.setListTimerObject(listTimer);
        return timerData;
    }

    public void removeData(Activity activity, TimerObject timer,
                           ArrayList<TimerObject> listTimers) {
        // TODO Auto-generated method stub
        for (int i = 0; i < listTimers.size(); i++) {
            if(listTimers.get(i).getId() == timer.getId()){
                listTimers.remove(i);
            }
        }
        TimerData timerData = new TimerData();
        timerData.setListTimerObject(listTimers);
        saveData(activity, timerData);
    }
    public void saveData(Context context , TimerData timerData){
        Log.i(TAG, "saveData: " + new Gson().toJson(
                timerData));
        FloatButtonTimerHelper.saveFloatButtonTimer(context, new Gson().toJson(
                timerData));
    }


    public ArrayList<TimerObject> getListTimerObject() {
        return listTimerObject;
    }
    public void setListTimerObject(ArrayList<TimerObject> listTimerObject) {
        this.listTimerObject = listTimerObject;
    }

}
