package com.project.a_team.wakeupper;

import android.app.Activity;
import android.os.Bundle;

public class UnblockActivity extends Activity {

    public static final String alarmSettings = "com.project.a_team.wakeupper.ALARM_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unblock);
    }
}
