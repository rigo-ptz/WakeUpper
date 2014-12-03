package com.project.a_team.wakeupper;

import android.content.ContentValues;


/**
 * Created by ef on 02.12.2014.
 */
public class Editor {
    public static Boolean createAlarm(Alarm alarm) {


        //TODO системный планировщик
        //TODO return false при ошибке или если все плохо
        return true;
    }

    public static void updateAlarm(Alarm alarm) {

    }
    public static void deleteAlarm(Integer alarmID) {

    }
    public static void changeState(Integer alarmID, Boolean state) {

    }

    private static ContentValues putValues(Alarm alarm) {
        ContentValues values = new ContentValues();
        //intState
        if(alarm.getState())
            values.put(DBHelper.STATE, 1);
        else
            values.put(DBHelper.STATE, 0);
        //txtDays
        values.put(DBHelper.DAYS, alarm.getDays());
        //intTime
        values.put(DBHelper.TIME, alarm.getTime().toMillis(false));
        //txtSignal
        values.put(DBHelper.SIGNAL, alarm.getSignal().toString());
        //intVibration
        if(alarm.getVibration())
            values.put(DBHelper.VIBRATION, 1);
        else
            values.put(DBHelper.VIBRATION, 0);
        //intVolume
        values.put(DBHelper.VOLUME, alarm.getVolume());
        //intActivity
        values.put(DBHelper.ACTIV, alarm.getActivity());

        return values;
    }
}
