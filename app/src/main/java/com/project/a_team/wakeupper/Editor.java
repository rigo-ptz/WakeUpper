package com.project.a_team.wakeupper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Created by ef on 02.12.2014.
 */
public class Editor {
    final static String LOG_TAG = "myLogs";

    public static Boolean createAlarm(Alarm alarm) {
        DBHelper dbHelper = new DBHelper(DBHelper.myContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues(putValues(alarm));

            long rowID = db.insert(DBHelper.TABLE_NAME, null, values);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);

            dbHelper.close();

            //TODO системный планировщик
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- Editor, createAlarm() ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public static void updateAlarm(Alarm alarm) {
        DBHelper dbHelper = new DBHelper(DBHelper.myContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues(putValues(alarm));

            int updCount = db.update(DBHelper.TABLE_NAME, values, "intId = ?",
                    new String[] { alarm.getID().toString() });
            Log.d(LOG_TAG, "updated rows count = " + updCount);

            dbHelper.close();

            //TODO системный планировщик
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- Editor, updateAlarm() ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

    }
    public static void deleteAlarm(Integer alarmID) {
        DBHelper dbHelper = new DBHelper(DBHelper.myContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            int delCount = db.delete(DBHelper.TABLE_NAME, "intId = " + alarmID.toString(), null);
            Log.d(LOG_TAG, "deleted rows count = " + delCount);

            dbHelper.close();

            //TODO системный планировщик
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- Editor, deleteAlarm() ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }


    }
    public static void changeState(Integer alarmID, Boolean state) {
        DBHelper dbHelper = new DBHelper(DBHelper.myContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues newState = new ContentValues();
            if(state)
                newState.put(DBHelper.STATE, 1);
            else
                newState.put(DBHelper.STATE, 0);

            int updCount = db.update(DBHelper.TABLE_NAME, newState,
                    "intId = " + alarmID.toString(), null);
            Log.d(LOG_TAG, "updated rows count = " + updCount);

            dbHelper.close();

            //TODO системный планировщик
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- Editor, changeState() ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

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
