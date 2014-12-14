package com.project.a_team.wakeupper;

/*
*   created by Yamushev Igor on 20.11.14
*   PetrSU, TPPO 2014. 22305 group
*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
        DataProvider.setContext(getApplicationContext());

        //находим элементы
        tvAdd = (TextView) findViewById(R.id.tvAddNewAlarm);

        //присваиваем обработчик
        tvAdd.setOnClickListener(this);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(getApplicationContext());

        ListView lvMain = (ListView) findViewById(R.id.lvMain);

        mAdapter = new AlarmListAdapter(this, DataProvider.getAlarms());

        lvMain.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddNewAlarm:
                if(countCheck()) {
                    break;
                }
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(alarmID, -1);
                startActivityForResult(intent, 0);
                break;

            default:
                break;
        }
    }

    protected boolean countCheck() {
        if(alarmCount >= 10) {
            Toast.makeText(this, "Только 10 будильников", Toast.LENGTH_LONG).show();
            changeVisible(true);
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
        Alarm alarm = DataProvider.getSettings(id);
        alarm.setState(isEnabled);
        Editor.updateAlarm(alarm);

        mAdapter.setAlarms(DataProvider.getAlarms());
        mAdapter.notifyDataSetChanged();

    }

    public void startSettingsActivity(int id) {
        Intent intent = new Intent(this, SettingsActivity.class);
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
                        //удаляем будильник из БД по id
                        Editor.deleteAlarm(alarmId);
                        //обновляем список будильников в адаптере
                        mAdapter.setAlarms(DataProvider.getAlarms());
                        //Уведомляем адаптер, что данные изменились
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();
    }
}
