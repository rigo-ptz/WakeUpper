package com.project.a_team.wakeupper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class UnblockActivity extends Activity {

    Alarm curAlarm;
    MediaPlayer player;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unblock);

        DataProvider.setContext(this);
        Editor.setContext(getApplicationContext());
        Alarm.setContext(getApplicationContext());

        Intent intent = getIntent();
        int alarmID = intent.getIntExtra(MainActivity.alarmID, -1);
        curAlarm = DataProvider.getSettings(alarmID);

        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        player = new MediaPlayer();
        try {
            player.setDataSource(getApplicationContext(), curAlarm.getSignal());
            player.setScreenOnWhilePlaying(true);
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, curAlarm.getVolume(),
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            player.prepare();
        } catch (Exception e) {
            Log.d("UnblockActivityLogs", "Failed to open URI");
            // TODO close app
        }

        player.start();
        unlockScreen();
    }

    public void unblockClick(View view) {
        player.stop();
    }

    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }
}
