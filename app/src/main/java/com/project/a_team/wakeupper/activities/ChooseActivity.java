package com.project.a_team.wakeupper.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.project.a_team.wakeupper.R;

public class ChooseActivity extends Activity {

    // Массив виджетов, отображающих числа
    private TextView[] intView;
    // Массив чисел, отображаемых виджетами
    private int[] ints;
    // Число, кратность которому проверяется
    private int div;
    // Количество чисел
    final private int amount = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        intView = new TextView[amount];
        intView[0] = (TextView)findViewById(R.id.intView1);
        intView[1] = (TextView)findViewById(R.id.intView2);
        intView[2] = (TextView)findViewById(R.id.intView3);
        intView[3] = (TextView)findViewById(R.id.intView4);
        intView[4] = (TextView)findViewById(R.id.intView5);
        intView[5] = (TextView)findViewById(R.id.intView6);
        intView[6] = (TextView)findViewById(R.id.intView7);
        intView[7] = (TextView)findViewById(R.id.intView8);
    }

    public void checkMultiplicity(View v) {

    }

    public void highlightView(View v) {

    }
}
