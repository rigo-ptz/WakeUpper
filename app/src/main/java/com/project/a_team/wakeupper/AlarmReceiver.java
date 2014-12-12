package com.project.a_team.wakeupper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiverLogs", "Alarm received");
        // Получаем номер будильника
        int alarmID = intent.getIntExtra(MainActivity.alarmID, -1);
        // Если номер будильника меньше 0 - будильника как-то даже и нету
        if (alarmID < 0) {
            return;
        }

        Log.d("AlarmReceiverLogs", "Alarm #" + alarmID + " received");

        // TODO Получаем информацию о будильнике из БД через DataProvider
        Alarm alarm = DataProvider.getSettings(alarmID);

        // TODO Запуск UnblockActivity с информацией о будильнике
        Intent unblockIntent = new Intent(context, UnblockActivity.class);
        unblockIntent.putExtra(MainActivity.alarmID, alarmID);
    }
}
