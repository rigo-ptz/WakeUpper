package com.project.a_team.wakeupper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project.a_team.wakeupper.activities.ArithmeticActivity;
import com.project.a_team.wakeupper.activities.ChooseActivity;
import com.project.a_team.wakeupper.activities.PhraseActivity;
import com.project.a_team.wakeupper.activities.ShakeActivity;

public class UnblockActivity extends Activity {

    int activityTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unblock);

        Intent intent = getIntent();
        activityTask = intent.getIntExtra(AlarmReceiver.alarmTask, 0);
        Log.d("UnblockActivityLogs", "alarmTask: " + Integer.toString(activityTask));

        unlockScreen(this);
    }

    public void unblockClick(View view) {
        Intent activityActivity;
        switch (activityTask) {
            case 1:
                activityActivity = new Intent(this, ArithmeticActivity.class);
                break;
            case 2:
                activityActivity = new Intent(this, ShakeActivity.class);
                break;
            case 3:
                activityActivity = new Intent(this, ChooseActivity.class);
                break;
            case 4:
                activityActivity = new Intent(this, PhraseActivity.class);
                break;
            default:
                turnOff();
                return;
        }

        startActivityForResult(activityActivity, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            turnOff();
        }
    }

    private void turnOff() {
        Intent notification = new Intent(getApplicationContext(), AlarmReceiver.AlarmNotification.class);
        stopService(notification);
        finish();
    }

    public static void unlockScreen(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
