package com.project.a_team.wakeupper.activities;

/*
*   created by Yamushev Igor on 04.11.14
*   PetrSU, TPPO 2014. 22305 group
*/

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.a_team.wakeupper.R;
import com.project.a_team.wakeupper.UnblockActivity;


public class ShakeActivity extends Activity implements SensorEventListener {

    private ImageView shakeImage;
    ProgressBar myProgressBar;
    // переменная текущего прогресса
    private int progress = 0;
    //переменная которая считает когда разность меньше (количество заходов в секцию else)
    private int falseSens = 0;
    SensorManager mSensorManager;
    //акселерометр
    Sensor accSensor;
    //feedback в виде вибрации, чтобы пользователь понимал, что заполнение идет
    Vibrator vibro;

    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake);

        /*// вывод изображения из ресурсов в ImageView
        shakeImage = (ImageView) findViewById(R.id.ivImg);
        shakeImage.setImageResource(R.drawable.shake);*/

        // нашли прогрессбар
        myProgressBar = (ProgressBar) findViewById(R.id.pbBar);

        // объект класса SensorManager для доступа ко всем сенсорам
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // получаем датчик типа акселерометр
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // получили вибрацию
        vibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        UnblockActivity.unlockScreen(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // подписываемся на получение данных от акселерометра
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // отписываемся от получения данных от акселерометра
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // проверяем, что изменилось состояние именно акселерометра
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // определим координаты
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //Log.d(TAG, "|(x*x + y*y + z*z) - const^2| " + Math.abs((x*x + y*y + z*z)- SensorManager.GRAVITY_EARTH  * SensorManager.GRAVITY_EARTH));

            //######################################################################################
            //#########     примерно от 30(легкая тряска) до 70(сильная тряска)       ##############
            //######################################################################################


            if ( Math.abs((x*x + y*y + z*z)- SensorManager.GRAVITY_EARTH  * SensorManager.GRAVITY_EARTH) > 30 ) {
                //увеличиваем и устанавливаем текущий прогресс
                progressStart();
                //проверяем на полноту ПБ
                progressCheck();
            } else {
                //считаем число заходов сюда, если больше 10, то обнуляем все
                criticalSection();
            }
        }
    }

    private void progressStart() {
        // вибрация на секунду
        vibro.vibrate(300);
        //начали снова заполнять, значит сбросить значение
        falseSens = 0;
        //увеличивается текущий прогресс. МАКСИМАЛЬНЫЙ  - 120
        progress++;
        myProgressBar.setProgress(progress);
    }

    private void progressCheck() {
        if (myProgressBar.getProgress() == myProgressBar.getMax()) {
            Toast.makeText(this, R.string.kurosawa, Toast.LENGTH_SHORT).show();
            onPause();
            vibro.cancel();
            setResult(RESULT_OK);
            finish();
        }
    }

    private void criticalSection() {
        falseSens++;
        vibro.cancel();
        if (falseSens == 10) {
            progress = 0;
            myProgressBar.setProgress(progress);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}