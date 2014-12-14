package com.project.a_team.wakeupper;

/*
*   created by Yamushev Igor on 20.11.14
*   PetrSU, TPPO 2014. 22305 group
*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener  {

    final String LOG_TAG = "myLogs";

    //кнопка добавления нового будильника
    static TextView tvAdd;

    DBHelper dbHelper;

    //адаптер для заполнения списка
    private AlarmListAdapter mAdapter;
    private Context mContext;

    // Для передачи в SettingsActivity
    public static final String alarmID = "com.project.a_team.wakeupper.ALARM_ID";

    public static int alarmCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Устанавливаем контекст в Editor
        Editor.setContext(getApplicationContext());
        Alarm.setContext(getApplicationContext());
        //
        DataProvider.setContext(getApplicationContext());
        mContext = this;

        //находим элементы
        tvAdd = (TextView) findViewById(R.id.tvAddNewAlarm);

        //присваиваем обработчик
        tvAdd.setOnClickListener(this);

        // создаем объект для создания и управления версиями БД
        //dbHelper = new DBHelper(this);
        dbHelper = new DBHelper(getApplicationContext());

        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        mAdapter = new AlarmListAdapter(this, DataProvider.getAlarms());

        //setListAdapter(mAdapter);
        lvMain.setAdapter(mAdapter);
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
                //Intent intent = new Intent(this, SettingsActivity.class);

                // TEST
                //intent.putExtra(alarmID, "-1");

                //alarmCount++;
                //Log.d(LOG_TAG, "--- MainActivity, onClick() ---");
                //Log.d(LOG_TAG, "increase alarmCount = " + alarmCount);
                if(countCheck()) {
                    break;
                }

                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(alarmID, -1);
                startActivityForResult(intent, 0);

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

    protected boolean countCheck() {
        if(alarmCount >= 10) {
            Toast.makeText(this, "Только 10 будильников", Toast.LENGTH_LONG).show();
            changeVisible(true);
            //tvAdd.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    public static void changeVisible(boolean isMore) {
        if(isMore) {
            tvAdd.setVisibility(View.INVISIBLE);
        } else {
            tvAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mAdapter.setAlarms(DataProvider.getAlarms());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setAlarmEnabled(int id, boolean isEnabled) {
        //AlarmManagerHelper.cancelAlarms(this);

        Alarm alarm = DataProvider.getSettings(id);
        alarm.setState(isEnabled);
        Editor.updateAlarm(alarm);

        mAdapter.setAlarms(DataProvider.getAlarms());
        mAdapter.notifyDataSetChanged();

        //AlarmManagerHelper.setAlarms(this);
    }

    public void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
        String str = Integer.toString(id);
        intent.putExtra(alarmID, id);
        startActivityForResult(intent, 0);
    }

    public void deleteAlarm(int id) {
        final int alarmId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Пожалуйста, подтвердите")
                .setTitle("Удалить выбранный будильник?")
                .setCancelable(true)
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //выключаем будильники в мэнеджере
                        //AlarmManagerHelper.cancelAlarms(mContext);
                        //удаляем будильник из БД по id
                        Editor.deleteAlarm(alarmId);
                        //обновляем список будильников в адаптере
                        mAdapter.setAlarms(DataProvider.getAlarms());
                        //Уведомляем адаптер, что данные изменились
                        mAdapter.notifyDataSetChanged();
                        //устанавливаем будильники
                        //AlarmManagerHelper.setAlarms(mContext);

                        //уменьшаем количество будильников
                        /*alarmCount--;
                        Log.d(LOG_TAG, "--- MainActivity, deleteAlarm() ---");
                        Log.d(LOG_TAG, "decrease alarmCount = " + alarmCount);*/
                    }
                }).show();
    }
}
