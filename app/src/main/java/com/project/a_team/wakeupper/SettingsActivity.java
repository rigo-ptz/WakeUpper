package com.project.a_team.wakeupper;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingsActivity extends Activity implements SeekBar.OnSeekBarChangeListener{

    private Alarm alarm;
    private Boolean newAlarm;
    private TextView timePickerLabel;
    private TextView signalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //посылаем в дата контекст для тоста. не удалять, иначе тост не покажется при ошибке
        DataProvider.setContext(this);

        Intent intent = getIntent();
        //String aID = intent.getStringExtra(MainActivity.alarmID);
        String aID = "1";
        newAlarm = aID.equals("-1");
            //Toast.makeText(this, "New alarm", Toast.LENGTH_SHORT).show();

        /* Слушатели */
        SeekBar volumeSeekBar = (SeekBar)findViewById(R.id.volume);
        volumeSeekBar.setOnSeekBarChangeListener(this);

        Switch vibrationSwitch = (Switch)findViewById(R.id.vibration);

        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    alarm.setVibration(true);
                } else {
                    alarm.setVibration(false);
                }
            }
        });

        if(newAlarm) {
            alarm = new Alarm();
        } else {
            alarm = DataProvider.getSettings(Integer.parseInt(aID));
        }

        /* Отобразить настройки на экране */

        /* Time */
        timePickerLabel = (TextView)findViewById(R.id.timePickerLabel);
        timePickerLabel.setText(alarm.getTime().format("%R"));

        /* Days */
        String days = alarm.getDays();
        if(days.charAt(0) == '1')
            ((CheckBox)findViewById(R.id.checkBox1)).setChecked(true);
        if(days.charAt(1) == '1')
            ((CheckBox)findViewById(R.id.checkBox2)).setChecked(true);
        if(days.charAt(2) == '1')
            ((CheckBox)findViewById(R.id.checkBox3)).setChecked(true);
        if(days.charAt(3) == '1')
            ((CheckBox)findViewById(R.id.checkBox4)).setChecked(true);
        if(days.charAt(4) == '1')
            ((CheckBox)findViewById(R.id.checkBox5)).setChecked(true);
        if(days.charAt(5) == '1')
            ((CheckBox)findViewById(R.id.checkBox6)).setChecked(true);
        if(days.charAt(6) == '1')
            ((CheckBox)findViewById(R.id.checkBox7)).setChecked(true);

        /* Volume */
        volumeSeekBar.setProgress(alarm.getVolume());
        AudioManager audioManager =
                (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));

        /* Vibration */
        ((Switch)findViewById(R.id.vibration)).setChecked(alarm.getVibration());

        /* Signal */
        signalTextView = (TextView)findViewById(R.id.signal);
        signalTextView.setText(alarm.getSignal().toString());

        /* Activity */
        switch (alarm.getActivity()) {
            case 0: ((RadioButton)findViewById(R.id.noActivity)).setChecked(true);
                break;
            case 1: ((RadioButton)findViewById(R.id.arithmetic)).setChecked(true);
                break;
            case 2: ((RadioButton)findViewById(R.id.shake)).setChecked(true);
                break;
            case 3: ((RadioButton)findViewById(R.id.choose)).setChecked(true);
                break;
            case 4: ((RadioButton)findViewById(R.id.phrase)).setChecked(true);
        }
    }

    public void setTime(View view) {
        int mHour = alarm.getTime().hour;
        int mMinute = alarm.getTime().minute;

        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Time time = new Time();
                        time.set(0, minute, hourOfDay, 0, 0, 0);
                        timePickerLabel.setText(time.format("%R"));
                        alarm.setTime(time);
                    }
                }, mHour, mMinute, true);
        tpd.setCancelable(true);
        tpd.show();
    }


    public void setDays(View view) {
        CheckBox daysPicker1 = (CheckBox)findViewById(R.id.checkBox1);
        CheckBox daysPicker2 = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox daysPicker3 = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox daysPicker4 = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox daysPicker5 = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox daysPicker6 = (CheckBox)findViewById(R.id.checkBox6);
        CheckBox daysPicker7 = (CheckBox)findViewById(R.id.checkBox7);

        StringBuilder days = new StringBuilder("0000000");

        if(daysPicker1.isChecked())
            days.setCharAt(0, '1');
        if(daysPicker2.isChecked())
            days.setCharAt(1, '1');
        if(daysPicker3.isChecked())
            days.setCharAt(2, '1');
        if(daysPicker4.isChecked())
            days.setCharAt(3, '1');
        if(daysPicker5.isChecked())
            days.setCharAt(4, '1');
        if(daysPicker6.isChecked())
            days.setCharAt(5, '1');
        if(daysPicker7.isChecked())
            days.setCharAt(6, '1');

        alarm.setDays(days.toString());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        alarm.setVolume(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void setSignalTextView(View view) {
        Intent intent=new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarm.getSignal());
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, alarm.getSignal());
        startActivityForResult(intent , 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    alarm.setSignal(ringtone);
                    signalTextView.setText(ringtone.toString());
                    break;

                default:
                    break;
            }
        }
    }


    public void setActivity(View view) {
        RadioButton activity1 = (RadioButton)findViewById(R.id.arithmetic);
        RadioButton activity2 = (RadioButton)findViewById(R.id.shake);
        RadioButton activity3 = (RadioButton)findViewById(R.id.choose);
        RadioButton activity4 = (RadioButton)findViewById(R.id.phrase);
        RadioButton activity5 = (RadioButton)findViewById(R.id.noActivity);

        Integer activity = 0;
        if(activity5.isChecked())
            activity = 0;
        if(activity1.isChecked())
            activity = 1;
        if(activity2.isChecked())
            activity = 2;
        if(activity3.isChecked())
            activity = 3;
        if(activity4.isChecked())
            activity = 4;

        alarm.setActivity(activity);
    }

    public void onCancelButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        builder.setTitle(R.string.confirm);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        builder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setMessage(R.string.confirmCancel);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onConfirmButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        builder.setTitle(R.string.confirm);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(newAlarm) {
                            if(!(Editor.createAlarm(alarm))) {
                                Toast.makeText(getApplicationContext(),
                                        R.string.errorOnCreateAlarm, Toast.LENGTH_SHORT).show();
                            } else {
                                finish();
                            }
                        } else {
                            Editor.updateAlarm(alarm);
                            finish();
                        }
                    }
                });

        builder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setMessage(R.string.confirmSave);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
