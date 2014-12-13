package com.project.a_team.wakeupper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Получаем номер будильника
        int alarmID = intent.getIntExtra(MainActivity.alarmID, -1);
        // Если номер будильника меньше 0 - будильника как-то даже и нету
        if (alarmID < 0) {
            return;
        }

        Log.d("AlarmReceiverLogs", "Alarm #" + alarmID + " received");

        // TODO Запуск UnblockActivity с информацией о будильнике
        Intent unblockIntent = new Intent(context, UnblockActivity.class);
        unblockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        unblockIntent.putExtra(MainActivity.alarmID, alarmID);
        context.startActivity(unblockIntent);
    }

    public void onUnblock(Context context, int result) {

    }

}
