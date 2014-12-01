package com.project.a_team.wakeupper;

/*
*   created by Yamushev Igor on 20.11.14
*   PetrSU, TPPO 2014. 22305 group
*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "myLogs";

    //кнопка добавления нового будильника
    TextView tvAdd;

    DBHelper dbHelper;

    // Для передачи в SettingsActivity
    public static final String alarmID = "com.project.a_team.wakeupper.ALARM_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //находим элементы
        tvAdd = (TextView) findViewById(R.id.tvAddNewAlarm);

        //присваиваем обработчик
        tvAdd.setOnClickListener(this);

        //вызываем построение списка ьбудильников
        showExistAlarms();

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    private void showExistAlarms() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddNewAlarm:
                /*//проверка
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(this, SettingsActivity.class);

                // TEST
                intent.putExtra(alarmID, "-1");

                // Testing ArithmeticActivity
                //Intent intent = new Intent(this, ArithmeticActivity.class);

                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public static final String DBNAME = "WakeUpperDB";
        public static final String TABLE_NAME = "tblAlarmSettings";
        public static final String ID = "intId ";
        public static final String STATE = "boolState";
        public static final String DAYS = "txtDays";
        public static final String VIBRATION= "boolVibration";
        public static final String VOLUME = "intVolume";
        public static final String ACTIV = "intActivity";

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table" + TABLE_NAME + "(intId integer primary key," +
                       "boolState integer," +
                       "txtDays text," +
                       "boolVibration integer," +
                       "intVolume integer," +
                       "intActivity intger);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
