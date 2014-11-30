package com.project.a_team.wakeupper;

import android.media.RingtoneManager;
import android.net.Uri;
import android.text.format.Time;

/**
 * Created by ef on 30.11.2014.
 */
public class Alarm {
    private Integer ID;
    private Boolean state;
    private Time time;
    private String days;
    private Uri signal;
    private Boolean vibration;
    private Integer volume;
    private Integer activity;

    public Alarm() {
        ID = -1;
        state = true;
        time = new Time();
        days = "0000000";
        signal = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        vibration = false;
        volume = 0;
        activity = 0;
    }

    public Alarm(Integer aID, Boolean aState, Time aTime, String aDays, Uri aSignal,
                 Boolean aVibration, Integer aVolume, Integer aActivity) {
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

    public Boolean getState(){
        return state;
    }

    public Time getTime(){
        return time;
    }

    public String getDays(){
        return days;
    }

    public Uri getSignal(){
        return signal;
    }

    public Boolean getVibration(){
        return vibration;
    }

    public Integer getVolume(){
        return volume;
    }

    public Integer getActivity(){
        return activity;
    }

    public void setState(Boolean aState) {
        state = aState;
    }

    public void setTime(Time aTime) {
        time = aTime;
    }

    public void setDays(String aDays) {
        days = aDays;
    }

    public void setSignal(Uri aSignal) {
        signal = aSignal;
    }

    public void setVibration(Boolean aVibration) {
        vibration = aVibration;
    }

    public void setVolume(Integer aVolume) {
        volume = aVolume;
    }

    public void setActivity(Integer aActivity) {
        activity = aActivity;
    }
}
