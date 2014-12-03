package com.project.a_team.wakeupper;

/*
*   created by Yamushev Igor on 20.11.14
*   PetrSU, TPPO 2014. 22305 group
*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public static final String DBNAME = "WakeUpperDB";
    public static final String TABLE_NAME = "tblAlarmSettings";
    public static final String ID = "_intId ";
    public static final String STATE = "intState";
    public static final String DAYS = "txtDays";
    public static final String TIME = "intTime";
    public static final String SIGNAL = "txtSignal";
    public static final String VIBRATION = "intVibration";
    public static final String VOLUME = "intVolume";
    public static final String ACTIV = "intActivity";
    public static Context myContext;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, DBNAME, null, 1);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID + " integer PRIMARY KEY," +
                STATE + " integer," +
                DAYS + " text," +
                TIME + " integer," +
                SIGNAL + " text," +
                VIBRATION +  " integer," +
                VOLUME + " integer," +
                ACTIV + " integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
