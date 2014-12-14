package com.project.a_team.wakeupper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
        //Log.d(LOG_TAG, "--- Editor, createAlarm() ---");
        //Log.d(LOG_TAG, "Create new alarm");
        DBHelper dbHelper = new DBHelper(appContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues(putValues(alarm));

            long rowID = db.insert(DBHelper.TABLE_NAME, null, values);
            //Log.d(LOG_TAG, "rowID = " + rowID);

            dbHelper.close();

            // добавление в сис. планировщик, если enabled
            if (alarm.getState()) {
                updateAlarmManager(alarm);
            }
        } catch (Exception ex) {
            //Log.d(LOG_TAG, "--- Editor, createAlarm() ---");
            //Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public static void updateAlarm(Alarm alarm) {
        //Log.d(LOG_TAG, "--- Editor, updateAlarm() ---");
        //Log.d(LOG_TAG, "Update alarm with ID="+alarm.getID());

        DBHelper dbHelper = new DBHelper(appContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues(putValues(alarm));

            int updCount = db.update(DBHelper.TABLE_NAME, values,
                    DBHelper.ID + " = " + alarm.getID().toString(), null);
            //Log.d(LOG_TAG, "updated rows count = " + updCount);

            dbHelper.close();

            // Если будильник будет включен - обновляем, если нет - пытаемся удалить
            if (alarm.getState()) {
                updateAlarmManager(alarm);
            } else {
                deleteAlarmFromManager(alarm.getID());
            }
        } catch (Exception ex) {
            //Log.d(LOG_TAG, "--- Editor, updateAlarm() ---");
            //Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }
    }

    public static void deleteAlarm(Integer alarmID) {
        //Log.d(LOG_TAG, "--- Editor, deleteAlarm() ---");
        //Log.d(LOG_TAG, "Delete alarm with ID="+alarmID);

        DBHelper dbHelper = new DBHelper(appContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();

            int delCount = db.delete(DBHelper.TABLE_NAME,
                    DBHelper.ID + " = " + alarmID.toString(), null);
            //Log.d(LOG_TAG, "deleted rows count = " + delCount);

            dbHelper.close();

            // безусловно удаляем будильник
            deleteAlarmFromManager(alarmID);
        } catch (Exception ex) {
            //Log.d(LOG_TAG, "--- Editor, deleteAlarm() ---");
            //Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }
    }

    public static void changeState(Integer alarmID, Boolean state) {
        DBHelper dbHelper = new DBHelper(appContext);
        SQLiteDatabase db;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues newState = new ContentValues();
            if (state) {
                newState.put(DBHelper.STATE, 1);
                updateAlarmManager(DataProvider.getSettings(alarmID));
            }
            else {
                newState.put(DBHelper.STATE, 0);
                deleteAlarmFromManager(alarmID);
            }

            int updCount = db.update(DBHelper.TABLE_NAME, newState,
                    DBHelper.ID + " = " + alarmID.toString(), null);
            //Log.d(LOG_TAG, "updated rows count = " + updCount);

            dbHelper.close();
        } catch (Exception ex) {
            //Log.d(LOG_TAG, "--- Editor, changeState() ---");
            //Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
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

    public static void unblockedAlarm(Alarm alarm) {
        if (!alarm.getDays().contains("0000000")) {
            updateAlarmManager(alarm);
        } else {
            changeState(alarm.getID(), false);
        }
    }

    private static void updateAlarmManager(Alarm alarm) {
        // Создаем интент будильника
        PendingIntent alarmIntent = createPendingIntent(alarm.getID());

        // Отменяем будильник, если есть
        alarmManager.cancel(alarmIntent);

        // Начинаем с текущего времени
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Сохраняем текущее время из календаря
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);

        // Если текущее время (ЧЧ:ММ) больше нужного, сразу добавляем день
        if (alarm.getTime().hour < nowHour  ||
                alarm.getTime().hour == nowHour && alarm.getTime().minute <= nowMinute) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        // Устанавливаем время срабатывания будильника (ЧЧ:ММ)
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getTime().hour);
        calendar.set(Calendar.MINUTE, alarm.getTime().minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Ищем день для срабатывания будильника
        if (!alarm.getDays().contains("0000000")) {
            int today = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            int day, dayCount;
            for (dayCount = 0; dayCount < 7; dayCount++) {
                day = (today + dayCount) % 7;
                if (alarm.getDays().charAt(day) == '1') {
                    break;
                }
            }
            calendar.add(Calendar.DAY_OF_WEEK, dayCount);
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        long msUntilAlarm = calendar.getTimeInMillis() - System.currentTimeMillis();
        //Log.d(LOG_TAG, "--- Editor, updateAlarmManager() ---");
        //Log.d(LOG_TAG, "Until alarm " + alarm.getID() + " alert: " + (msUntilAlarm/1000) + " sec");
        Toast.makeText(appContext, "Будильник сработает через " + (msUntilAlarm)/1000 + " секунд", Toast.LENGTH_LONG).show();
    }

    private static void deleteAlarmFromManager(int alarmID) {
        // Создаем интент будильника
        PendingIntent alarmIntent = createPendingIntent(alarmID);

        // Отменяем будильник если есть
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
        //Log.d(LOG_TAG, "--- Editor, setContext() ---");
        appContext = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}
