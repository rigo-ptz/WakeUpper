package com.project.a_team.wakeupper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
*   created by Yamushev Igor on 03.12.14
*   PetrSU, TPPO 2014. 22305 group
*/
public class DataProvider{

    final String LOG_TAG = "myLogs";

    DBHelper dbHelper = new DBHelper(DBHelper.myContext);
    SQLiteDatabase db;

    // можно и сразу все конструктору отдать
    Alarm alarm = new Alarm();

    public Alarm getSettings(Integer alarmID) {

        // подключаемся к БД
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception ex) {
            Log.d(LOG_TAG, "--- DataProvider, getSettings ---");
            Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
        }

        // --- ДЕЛАЕМ ЗАПРОС ---
        Cursor c = db.query(DBHelper.TABLE_NAME, new String[] {DBHelper.ID, DBHelper.STATE, DBHelper.DAYS,
                                                               DBHelper.TIME, DBHelper.SIGNAL, DBHelper.VIBRATION,
                                                               DBHelper.VOLUME, DBHelper.ACTIV},
                            DBHelper.ID + " = " + alarmID.toString(), null, null, null, null);

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

                alarm.setID(idFromDB);
                alarm.setState(stateFromDB);
                alarm.setDays(daysFromDB);
                alarm.setTime(timeFromDB);
                alarm.setSignal(signalFromDB);
                alarm.setVibration(vibroFromDB);
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

    public List getIDs() {
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
}
