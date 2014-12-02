package com.project.a_team.wakeupper.activities;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.a_team.wakeupper.R;

import java.util.Random;

public class ChooseActivity extends Activity {

    // Виджет описания задачи
    private TextView descView;
    // Ресурсы
    Resources resources;
    // Массив виджетов, отображающих числа
    private TextView[] intView;
    // Массив чисел, отображаемых виджетами
    private int[] ints;
    // Число, кратность которому проверяется
    private int div;
    // Рандомные числа!
    private Random random;
    // Количество чисел
    final private int amount = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);
        intView = new TextView[amount];
        intView[0] = (TextView)findViewById(R.id.intView1);
        intView[1] = (TextView)findViewById(R.id.intView2);
        intView[2] = (TextView)findViewById(R.id.intView3);
        intView[3] = (TextView)findViewById(R.id.intView4);
        intView[4] = (TextView)findViewById(R.id.intView5);
        intView[5] = (TextView)findViewById(R.id.intView6);
        intView[6] = (TextView)findViewById(R.id.intView7);
        intView[7] = (TextView)findViewById(R.id.intView8);
        descView = (TextView)findViewById(R.id.chooseDesc);
        ints = new int[amount];
        random = new Random();
        resources = getResources();
        generateTask();
    }

    private void generateTask() {
        div = random.nextInt(5) + 2;
        for (int i = 0; i < amount; i++) {
            ints[i] = random.nextInt(900) + 100;
            intView[i].setText(Integer.toString(ints[i]));
            intView[i].setBackgroundColor(resources.getColor(R.color.backgroundColor));
        }
        descView.setText(resources.getString(R.string.choose_description) + ' '
                + Integer.toString(div));

    }

    public void checkMultiplicity(View v) {
        boolean checked;
        for (int i = 0; i < amount; i++) {
            if (((ColorDrawable)intView[i].getBackground()).getColor() ==
                                resources.getColor(R.color.highlightColor)) {
                checked = true;
            } else {
                checked = false;
            }

            if (ints[i]%div == 0 && !checked ||
                ints[i]%div != 0 && checked) {
                Toast.makeText(this, R.string.tryTryAgain, Toast.LENGTH_LONG).show();
                generateTask();
                return;
            }
        }
        Toast.makeText(this, R.string.kurosawa, Toast.LENGTH_SHORT).show();
    }

    public void highlightView(View v) {
        int color = ((ColorDrawable) v.getBackground()).getColor();
        if (color == resources.getColor(R.color.backgroundColor)) {
            v.setBackgroundColor(resources.getColor(R.color.highlightColor));
        } else {
            v.setBackgroundColor(resources.getColor(R.color.backgroundColor));
        }
    }
}
