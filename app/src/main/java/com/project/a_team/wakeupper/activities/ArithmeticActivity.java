package com.project.a_team.wakeupper.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.a_team.wakeupper.R;
import com.project.a_team.wakeupper.UnblockActivity;

import java.util.Random;

public class ArithmeticActivity extends Activity {

    private int equationSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arithmetic);

        generateEquation();
        UnblockActivity.unlockScreen(this);
    }


    public void onConfirmButtonClick(View view) {
        EditText answer = (EditText)findViewById(R.id.inputAnswer);

        if(answer.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.enterAnswer, Toast.LENGTH_LONG).show();
            return;
        }

        int answerNumber = Integer.parseInt(answer.getText().toString());
        answer.setText("");

        if(!(checkEquation(answerNumber))) {
            Toast.makeText(this, R.string.incorrectTryAgain, Toast.LENGTH_LONG).show();
            generateEquation();
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void generateEquation() {
        Random rand = new Random();
        int max = 999, min = 100;
        Integer equationX = rand.nextInt(max - min + 1) + min;
        Integer equationY = rand.nextInt(max - min + 1) + min;
        Character equationSign;
        if(0 == rand.nextInt(2)) {
            equationSign = '+';
            equationSum = equationX + equationY;
        } else {
            equationSign = '-';
            equationSum = equationX - equationY;
        }

        TextView task = (TextView)findViewById(R.id.task);
        task.setText(equationX.toString() + " " + equationSign + " " + equationY.toString());
    }

    private Boolean checkEquation(int answerNumber) {
        return (answerNumber == equationSum);
    }
}
