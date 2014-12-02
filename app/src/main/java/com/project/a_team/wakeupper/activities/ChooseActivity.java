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
    }

    public void checkMultiplicity(View v) {

    }

    public void highlightView(View v) {

    }
}
