package com.supho.model;

/**
 * Created by Khai Tran on 5/26/2017.
 */

public class TimerObject {
    private int id;
    private Long startTime;
    private Long endTime;

    public TimerObject(){
    }
    public TimerObject(int id , Long start , Long end){
        this.id = id ;
        this.startTime = start;
        this.endTime = end;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
