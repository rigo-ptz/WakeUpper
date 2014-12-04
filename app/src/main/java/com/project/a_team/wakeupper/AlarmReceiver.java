package com.project.a_team.wakeupper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Получаем номер будильника
        int alarmID = intent.getIntExtra(MainActivity.alarmID, -1);
        // Если номер будильника меньше 0 - будильника как-то даже и нету
        if (alarmID < 0) {
            return;
        }

        // TODO Получаем информацию о будильнике из БД через DataProvider

        // TODO Запуск UnblockActivity с информацией о будильнике
    }
}
