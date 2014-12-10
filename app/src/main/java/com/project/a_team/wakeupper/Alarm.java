package com.project.a_team.wakeupper;

import android.app.AlarmManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;

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

    private static Context context;

    public Alarm() {
        ID = -1;
        state = true;
        time = new Time();
        days = "0000000";
        signal = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        vibration = false;

        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) / 2;

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

    public Integer getTimeInSeconds() { return time.hour * 60 * 60 + time.minute * 60; }

    // Yamushev Igor 03.12.14
    public void setID(Integer ID) { this.ID = ID; }

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

    public void setTimeFromSeconds(Integer seconds) {
        time.hour = seconds / 3600;
        time.minute = seconds % 3600 / 60;
        Log.d("log", "--- alarm " + time.hour + " " + time.minute);
    }

    public boolean getRepeatingDay(int dayOfWeek) {
        dayOfWeek = (dayOfWeek + 5) % 7;
        if (days.charAt(dayOfWeek) == '1')
            return true;
        else
            return false;
    }

    public static void setContext(Context ctx) {
        context = ctx;
    }
}
