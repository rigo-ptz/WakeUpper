package com.project.a_team.wakeupper;

/*
*   created by Yamushev Igor on 20.11.14
*   PetrSU, TPPO 2014. 22305 group
*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

                /*// подключаемся к БД
                try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                } catch (Exception ex) {
                    Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
                }
                // закрываем подключение к БД
                dbHelper.close();*/

                break;
            default:
                break;
        }
    }
}
