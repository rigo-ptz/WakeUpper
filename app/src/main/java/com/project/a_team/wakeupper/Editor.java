package com.project.a_team.wakeupper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by ef on 02.12.2014.
 */
public class Editor {
    final static String LOG_TAG = "myLogs";

    // Для работы с AlarmManager
    static Context appContext;
    static AlarmManager alarmManager;

    public static Boolean createAlarm(Alarm alarm) {
        Log.d(LOG_TAG, "--- Editor, createAlarm() ---");
        Log.d(LOG_TAG, "Create new alarm");
        DBHelper dbHelper = new DBHelper(DBHelper.myContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues(putValues(alarm));

            long rowID = db.insert(DBHelper.TABLE_NAME, null, values);
            Log.d(LOG_TAG, "rowID = " + rowID);

            dbHelper.close();

            // TODO сис. планировщик
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- Editor, createAlarm() ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public static void updateAlarm(Alarm alarm) {
        Log.d(LOG_TAG, "--- Editor, updateAlarm() ---");
        Log.d(LOG_TAG, "Update alarm with ID="+alarm.getID());

        DBHelper dbHelper = new DBHelper(DBHelper.myContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues(putValues(alarm));

            int updCount = db.update(DBHelper.TABLE_NAME, values,
                    DBHelper.ID + " = " + alarm.getID().toString(), null);
            Log.d(LOG_TAG, "updated rows count = " + updCount);

            dbHelper.close();

            // TODO сис. планировщик
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- Editor, updateAlarm() ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

    }

    public static void deleteAlarm(Integer alarmID) {
        Log.d(LOG_TAG, "--- Editor, deleteAlarm() ---");
        Log.d(LOG_TAG, "Delete alarm with ID="+alarmID);

        DBHelper dbHelper = new DBHelper(DBHelper.myContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            int delCount = db.delete(DBHelper.TABLE_NAME,
                    DBHelper.ID + " = " + alarmID.toString(), null);
            Log.d(LOG_TAG, "deleted rows count = " + delCount);

            dbHelper.close();

            deleteAlarmFromManager(alarmID);
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
            if (state) {
                newState.put(DBHelper.STATE, 1);
                // TODO добавл. в сис. план. ЛУЧШЕ
            }
            else {
                newState.put(DBHelper.STATE, 0);
                // TODO удаление из сис. план.
            }

            int updCount = db.update(DBHelper.TABLE_NAME, newState,
                    DBHelper.ID + " = " + alarmID.toString(), null);
            Log.d(LOG_TAG, "updated rows count = " + updCount);

            dbHelper.close();

        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- Editor, changeState() ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

    }

    private static ContentValues putValues(Alarm alarm) {
        ContentValues values = new ContentValues();
        //intState
        if (alarm.getState())
            values.put(DBHelper.STATE, 1);
        else
            values.put(DBHelper.STATE, 0);
        //txtDays
        values.put(DBHelper.DAYS, alarm.getDays());
        //intTime
        values.put(DBHelper.TIME, alarm.getTimeInSeconds());
        //txtSignal
        values.put(DBHelper.SIGNAL, alarm.getSignal().toString());
        //intVibration
        if (alarm.getVibration())
            values.put(DBHelper.VIBRATION, 1);
        else
            values.put(DBHelper.VIBRATION, 0);
        //intVolume
        values.put(DBHelper.VOLUME, alarm.getVolume());
        //intActivity
        values.put(DBHelper.ACTIV, alarm.getActivity());

        return values;
    }

    private static void addAlarmToManager(Alarm alarm) {
        PendingIntent alarmIntent = createPendingIntent(alarm.getID());

        // Танцуем с бубном чтобы найти следующую дату
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getTime().hour);
        calendar.set(Calendar.MINUTE, alarm.getTime().minute);
        calendar.set(Calendar.SECOND, 00);

        //Find next time to set
        final int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        final int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
        boolean repeatable = alarm.getDays().contains("0000000");

        boolean alarmSet = false;

        // Устанавливаем будильник
        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
            if (alarm.getRepeatingDay(dayOfWeek) && dayOfWeek >= nowDay &&
                    !(dayOfWeek == nowDay && alarm.getTime().hour < nowHour) &&
                    !(dayOfWeek == nowDay && alarm.getTime().hour == nowHour && alarm.getTime().minute <= nowMinute)) {
                calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                Toast.makeText(appContext, "БУДИЛЬНИК УСТАНОВЛЕН", Toast.LENGTH_LONG).show();
                alarmSet = true;
                break;
            }
        }

        //Else check if it's earlier in the week
        if (!alarmSet) {
            for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                if (alarm.getRepeatingDay(dayOfWeek - 1) && dayOfWeek <= nowDay && repeatable) {
                    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    Toast.makeText(appContext, "БУДИЛЬНИК УСТАНОВЛЕН", Toast.LENGTH_LONG).show();
                    alarmSet = true;
                    break;
                }
            }
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, 0, alarmIntent);
    }

    private static void deleteAlarmFromManager(int alarmID) {
        PendingIntent alarmIntent = createPendingIntent(alarmID);

        // Удаляем будильник
        alarmManager.cancel(alarmIntent);
    }

    private static PendingIntent createPendingIntent(int alarmID) {
        // Интент срабатывания будильника
        Intent rcvIntent = new Intent(appContext, AlarmReceiver.class);

        // Кладем в интент минимальную информацию о будильнике
        rcvIntent.putExtra(MainActivity.alarmID, alarmID);

        // Получаем PendingIntent
        return PendingIntent.getBroadcast(appContext, alarmID, rcvIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void setContext(Context context) {
        appContext = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}
