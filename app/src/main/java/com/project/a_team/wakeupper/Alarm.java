package com.project.a_team.wakeupper;

/**
 * Created by ef on 30.11.2014.
 */
public class Alarm {
    private Integer ID;
    private int state;
    private int time;
    private String days;
    private String signal;
    private int vibration;
    private Integer volume;
    private Integer activity;

    public Alarm() {
        ID = -1;
        state = 1;
        //time = new Time();  TODO написать перевод времени
        time = 0;
        days = "0000000";
        //signal = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE); TODO привести это к строке, и обратно
        vibration = 0;
        volume = 0;
        activity = 0;
    }

    public Alarm(Integer aID, int aState, int aTime, String aDays, String aSignal,
                 int aVibration, Integer aVolume, Integer aActivity) {
        ID = aID;
        state = aState;
        time = aTime;
        days = aDays;
        signal = aSignal;
        vibration = aVibration;
        volume = aVolume;
        activity = aActivity;
    }

    public Integer getID(){
        return ID;
    }

    public int getState(){
        return state;
    }

    public int getTime(){
        return time;
    }

    public String getDays(){
        return days;
    }

    public String getSignal(){
        return signal;
    }

    public int getVibration(){
        return vibration;
    }

    public Integer getVolume(){
        return volume;
    }

    public Integer getActivity(){
        return activity;
    }

    // Yamushev Igor 03.12.14
    public void setID(Integer ID) { this.ID = ID; }

    public void setState(int aState) {
        state = aState;
    }

    public void setTime(int aTime) {
        time = aTime;
    }

    public void setDays(String aDays) {
        days = aDays;
    }

    public void setSignal(String aSignal) {
        signal = aSignal;
    }

    public void setVibration(int aVibration) {
        vibration = aVibration;
    }

    public void setVolume(Integer aVolume) {
        volume = aVolume;
    }

    public void setActivity(Integer aActivity) {
        activity = aActivity;
    }
}
