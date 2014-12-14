package com.project.a_team.wakeupper;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

/*
*   created by Yamushev Igor on 03.12.14
*   PetrSU, TPPO 2014. 22305 group
*/
public class AlarmListAdapter extends BaseAdapter{
    final static String LOG_TAG = "myLogs";
    private Context mContext;
    private List<Alarm> mAlarms;

    public AlarmListAdapter(Context context, List<Alarm> alarms) {
        mContext = context;
        mAlarms = alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        mAlarms = alarms;
    }

    @Override
    public int getCount() {
        if (mAlarms != null) {
            MainActivity.alarmCount = mAlarms.size();
            //Log.d(LOG_TAG, "--- AlarmListAdapter, getCount() ---");
            //Log.d(LOG_TAG, "alarmCount from getCount = " + MainActivity.alarmCount);
            if(MainActivity.alarmCount <= 10) {
                MainActivity.changeVisible(false);
            }
            return mAlarms.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mAlarms != null) {
            return mAlarms.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mAlarms != null) {
            return mAlarms.get(position).getID();
        }
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_list_item, parent, false); // созданный View-элемент получит LayoutParams от root-элемента, но не добавится к нему.
        }

        Alarm alarm = (Alarm) getItem(position);

        TextView txtTime = (TextView) view.findViewById(R.id.alarm_item_time);
        txtTime.setText(String.format("%02d : %02d", alarm.getTime().hour, alarm.getTime().minute));

        char[] days = alarm.getDays().toCharArray();
        //Log.d(LOG_TAG, "--- Изменение цвета дней ---");
        //Log.d(LOG_TAG, "--- Все дни ---" + days[0] + " "+ days[1] + " "+ days[2] + " "+ days[3] + " "+ days[4] + " "+ days[5] + " "+ days[6] + " ");

        updateTextColor((TextView) view.findViewById(R.id.alarm_item_sunday), days[0] == '1');
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_monday), days[1] == '1');
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_tuesday), days[2] == '1');
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_wednesday), days[3] == '1');
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_thursday), days[4] == '1');
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_friday), days[5] == '1');
        updateTextColor((TextView) view.findViewById(R.id.alarm_item_saturday), days[6] == '1');

        ToggleButton btnToggle = (ToggleButton) view.findViewById(R.id.alarm_item_toggle);
        btnToggle.setTag(alarm.getID()); // связываем кнопку вкл. с ID будильника

        btnToggle.setChecked(alarm.getState());

        btnToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((MainActivity) mContext).setAlarmEnabled((Integer) buttonView.getTag(), isChecked);
            }
        });

        view.setTag(alarm.getID()); // связываем view с ID будильника

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    ((MainActivity) mContext).startSettingsActivity((Integer) view.getTag());
                } catch (Exception ex) {
                    //Log.d(LOG_TAG, "--- AlarmListAdapter, onClick ---" + view.getTag().toString());
                    Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
                }
            }
        });

        view.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                try {
                    ((MainActivity) mContext).deleteAlarm((Integer) view.getTag());
                } catch (Exception ex) {
                    //Log.d(LOG_TAG, "--- AlarmListAdapter, onLongClick ---");
                    Log.d(LOG_TAG, ex.getClass() + " error: " + ex.getMessage());
                }
                return true;
            }
        });

        return view;
    }

    private void updateTextColor(TextView view, boolean isOn) {
        if (isOn) {
            view.setTextColor(Color.GREEN);
        } else {
            view.setTextColor(Color.BLACK);
        }
    }

}
