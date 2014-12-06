package com.project.a_team.wakeupper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
*   created by Yamushev Igor on 03.12.14
*   PetrSU, TPPO 2014. 22305 group
*/
public abstract class DataProvider{

    final static String LOG_TAG = "myLogs";

    static DBHelper dbHelper = new DBHelper(DBHelper.myContext);
    static SQLiteDatabase db;
    static Context myContext;


    // TODO static method please . FIX
    public static Alarm getSettings(Integer alarmID) {

        // можно и сразу все конструктору отдать внизу, а можно и сеттерами
        // но так лучше, если ошибка, то вернется версия по умолчанию
        Alarm alarm = new Alarm();

        // подключаемся к БД
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- DataProvider, getSettings ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

        // --- ДЕЛАЕМ ЗАПРОС ---
        Cursor c = null;
        try {
            c = db.query(DBHelper.TABLE_NAME, new String[] {DBHelper.ID, DBHelper.STATE, DBHelper.DAYS,
                                                                   DBHelper.TIME, DBHelper.SIGNAL, DBHelper.VIBRATION,
                                                                   DBHelper.VOLUME, DBHelper.ACTIV},
                                DBHelper.ID + " = " + alarmID.toString(), null, null, null, null);
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- DataProvider, getSettings, WRONG ID ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
            Toast.makeText(myContext,R.string.noSuchIDError, Toast.LENGTH_LONG).show();
            return alarm;
        }

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(DBHelper.ID);
            int stateColIndex = c.getColumnIndex(DBHelper.STATE);
            int daysColIndex = c.getColumnIndex(DBHelper.DAYS);
            int timeColIndex = c.getColumnIndex(DBHelper.TIME);
            int signalColIndex = c.getColumnIndex(DBHelper.SIGNAL);
            int vibroColIndex = c.getColumnIndex(DBHelper.VIBRATION);
            int volumeColIndex = c.getColumnIndex(DBHelper.VOLUME);
            int activColIndex = c.getColumnIndex(DBHelper.ACTIV);

            do {
                // получаем значения по номерам столбцов  и зполняем alarm
                int idFromDB = c.getInt(idColIndex);
                int stateFromDB = c.getInt(stateColIndex);
                String daysFromDB = c.getString(daysColIndex);
                int timeFromDB = c.getInt(timeColIndex);
                String signalFromDB = c.getString(signalColIndex);
                int vibroFromDB = c.getInt(vibroColIndex);
                int volumeFromDB = c.getInt(volumeColIndex);
                int activFromDB = c.getInt(activColIndex);

                Time myTime = new Time();
                myTime.set(timeFromDB); // если что переписать ручной перевод времени

                alarm.setID(idFromDB);
                alarm.setState(toBoolean(stateFromDB));
                alarm.setDays(daysFromDB);
                alarm.setTime(myTime);
                alarm.setSignal(Uri.parse(signalFromDB)); // ?
                alarm.setVibration(toBoolean(vibroFromDB));
                alarm.setVolume(volumeFromDB);
                alarm.setActivity(activFromDB);
            } while (c.moveToNext());
            c.close();
        } else {
            Log.d(LOG_TAG, "--- DataProvider, getIDs ---");
            Log.d(LOG_TAG, "0 rows");
            c.close();
        }

        // закрываем подключение к БД
        dbHelper.close();

        return alarm;
    }

    private static Boolean toBoolean(int x) {
        Boolean res;
        res = x == 1;
        return res;
    }

    public static List getIDs() {
        // массив заполненный ИД
        List<Integer> IDList = new ArrayList<Integer>();

        // подключаемся к БД
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- DataProvider, getIDs ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

        /*
        *  --- ДЕЛАЕМ ЗАПРОС ---
        *
        * имя таблицы
        * список имен возвращаемых полей. При передаче null возвращаются все столбцы
        * параметр, формирующий выражение WHERE (исключая сам оператор WHERE). Значение null возвращает все строки.
        * значения аргументов фильтра;
        * параметр, формирующий выражение GROUP BY (исключая сам оператор GROUP BY). Если GROUP BY не нужен, передается null;
        * параметр, формирующий выражение HAVING (исключая сам оператор HAVING). Если не нужен, передается null;
        * параметр, форматирующий выражение ORDER BY (исключая сам оператор ORDER BY). При сортировке по умолчанию передается null.
        *
        * */
        Cursor c = db.query(DBHelper.TABLE_NAME, new String[] {DBHelper.ID}, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(DBHelper.ID);

            do {
                // получаем значения по номерам столбцов  и зполняем list
                int idFromDB = c.getInt(idColIndex);
                IDList.add(idFromDB);
            } while (c.moveToNext());
            c.close();
        } else {
            Log.d(LOG_TAG, "--- DataProvider, getIDs ---");
            Log.d(LOG_TAG, "0 rows");
            c.close();
        }

        // закрываем подключение к БД
        dbHelper.close();

        return IDList;
    }

    public static void setContext(Context context) {
        myContext = context;
    }

    private static Alarm fillAlarm(Cursor c) {
        Alarm alarm = new Alarm();
        alarm.setID(c.getInt(c.getColumnIndex(DBHelper.ID)));
        alarm.setState(toBoolean(c.getInt(c.getColumnIndex(DBHelper.STATE))));
        alarm.setDays(c.getString(c.getColumnIndex(DBHelper.DAYS)));

        int timeFromDB = c.getInt(c.getColumnIndex(DBHelper.TIME));
        //Time myTime = new Time();
        //myTime.set(timeFromDB); // если что переписать ручной перевод времени
        //alarm.setTime(myTime);
        alarm.setTimeFromSeconds(timeFromDB);

        alarm.setSignal(Uri.parse(c.getString(c.getColumnIndex(DBHelper.SIGNAL)))); // ?
        alarm.setVibration(toBoolean(c.getInt(c.getColumnIndex(DBHelper.VIBRATION))));
        alarm.setVolume(c.getInt(c.getColumnIndex(DBHelper.VOLUME)));
        alarm.setActivity(c.getInt(c.getColumnIndex(DBHelper.ACTIV)));
        return alarm;
    }

    public static List<Alarm> getAlarms() {
        // подключаемся к БД
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- DataProvider, getAlarms ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

        String select = "SELECT * FROM " + DBHelper.TABLE_NAME;

        Cursor c = db.rawQuery(select, null);
        //Cursor c = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        Log.d(LOG_TAG, "--- DataProvider, getAlarms ---");
        Log.d(LOG_TAG, String.valueOf(c.getColumnIndex(DBHelper.ID)));

        List<Alarm> alarmList = new ArrayList<Alarm>();

/*        while (c.moveToNext()) {
            alarmList.add(fillAlarm(c));
        }*/

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            do{
                alarmList.add(fillAlarm(c));
            } while (c.moveToNext());
        } else {
            Log.d(LOG_TAG, "--- DataProvider, getAlarms ---");
            Log.d(LOG_TAG, "0 rows");
            c.close();
        }

        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        db.close();
        return null;
    }
}
