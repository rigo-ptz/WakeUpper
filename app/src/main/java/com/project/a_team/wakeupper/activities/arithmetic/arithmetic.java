package com.project.a_team.wakeupper.activities.arithmetic;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.Random;
import android.widget.EditText;
import android.widget.TextView;
import com.project.a_team.wakeupper.R;

import org.w3c.dom.Text;



public class arithmetic extends Activity {

    private int equationSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arithmetic);

        generateEquation();

    }

    public void onConfirmButtonClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(arithmetic.this);
        builder.setTitle(R.string.alertTitle)
                .setCancelable(false)
                .setNegativeButton(R.string.stringOK,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        EditText answer = (EditText)findViewById(R.id.inputAnswer);

        if(answer.getText().toString().isEmpty()) {
            builder.setMessage(R.string.enterAnswer);
            AlertDialog alert = builder.create();
            alert.show();

            return;
        }

        int answerNumber = Integer.parseInt(answer.getText().toString());
        answer.setText("");

        if(!(checkEquation(answerNumber))) {
            builder.setMessage(R.string.incorrectTryAgain);
            AlertDialog alert = builder.create();
            alert.show();

            generateEquation();
        } else {
            builder.setMessage(R.string.correctAnswer);
            AlertDialog alert = builder.create();
            alert.show();
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

        TextView task = (TextView)findViewById(R.id.task_equation);
        task.setText(equationX.toString() + " " + equationSign + " " + equationY.toString());
    }

    private boolean checkEquation(int answerNumber) {
        return (answerNumber == equationSum);
    }
}
