package com.project.a_team.wakeupper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import android.os.Vibrator;
import android.provider.ContactsContract;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{

    public static final String alarmTask = "com.project.a_team.wakeupper.ALARM_TASK";
    public static final String alarmReceiver = "com.project.a_team.wakeupper.ALARM_RECEIVER_CLASS";

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
        Intent serviceIntent = new Intent(context, AlarmNotification.class);
        serviceIntent.putExtra(MainActivity.alarmID, alarmID);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(serviceIntent);
    }

    public static class AlarmNotification extends Service {

        AudioManager audioManager;
        MediaPlayer player;
        Alarm curAlarm;
        Intent unblockIntent;
        NotificationManager notificationManager;
        Notification notification;
        Vibrator vibrator;

        public void onCreate() {
            super.onCreate();
            Log.d("AlarmReceiverLogs", "onCreate");
        }

        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("AlarmReceiverLogs", "onStartCommand");

            // Setting contexts
            Alarm.setContext(getApplicationContext());
            DataProvider.setContext(getApplicationContext());
            Editor.setContext(getApplicationContext());

            // Getting Alarm
            curAlarm = DataProvider.getSettings(intent.getIntExtra(MainActivity.alarmID, -1));

            Editor.unblockedAlarm(curAlarm);

            // Working with audio
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            player = new MediaPlayer();
            try {
                player.setDataSource(getApplicationContext(), curAlarm.getSignal());
                player.setScreenOnWhilePlaying(true);
                player.setAudioStreamType(AudioManager.STREAM_ALARM);
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM, curAlarm.getVolume(),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                player.setLooping(true);
                player.prepare();
            } catch (Exception e) {
                Log.d("UnblockActivityLogs", "Failed to open URI");
            }
            player.start();

            // Vibro
            if (curAlarm.getVibration()) {
                long pattern[] = {300, 300};
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(pattern, 0);
            }

            // Creating UnblockActivity Intent
            unblockIntent = new Intent(getApplicationContext(), UnblockActivity.class);
            unblockIntent.putExtra(AlarmReceiver.alarmTask, curAlarm.getActivity());
            unblockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(unblockIntent);

            // Working with notification
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(getApplicationContext());
            builder.setSmallIcon(R.drawable.alert);
            builder.setContentTitle(getResources().getString(R.string.app_name));
            builder.setContentText(getResources().getString(R.string.unblock));
            builder.setOngoing(true);
            builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, unblockIntent, 0));
            if (Build.VERSION.SDK_INT < 16) {
                notification = builder.getNotification();
                Log.d("UnblockActivityLogs", "SDK 15");
            } else {
                notification = builder.build();
                Log.d("UnblockActivityLogs", "SDK 16+");
            }

            if (notification != null) {
                startForeground(1, notification);
            }

            return START_NOT_STICKY;
        }

        public void onDestroy() {
            super.onDestroy();
            player.stop();
            if (vibrator != null) {
                vibrator.cancel();
            }
            this.stopForeground(false);
            Log.d("AlarmReceiverLogs", "onDestroy");
        }

        public IBinder onBind(Intent intent) {
            Log.d("AlarmReceiverLogs", "onBind");
            return null;
        }
    }
}
