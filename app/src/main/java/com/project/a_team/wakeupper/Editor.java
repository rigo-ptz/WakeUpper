package com.project.a_team.wakeupper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ef on 02.12.2014.
 */
public class Editor {
    private static SQLiteDatabase openDB() {
        //SQLiteDatabase.openDatabase()

    }


    public static Boolean createAlarm(Alarm alarm) {
        SQLiteDatabase db = openDB();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            //intState
            //txtDays
            //intTime
            //txtSignal
            //intVibration
            //intVolume
            //intActivity
            values.put(MainActivity.DBHelper.STATE, Integer.valueOf(1));



            db.insertOrThrow(MainActivity.DBHelper.TABLE_NAME, "", values);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public static void updateAlarm(Alarm alarm) {

    }
    public static void deleteAlarm(Integer alarmID) {

    }
    public static void changeState(Integer alarmID, Boolean state) {

    }

}
