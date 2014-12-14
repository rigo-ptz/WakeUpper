package com.project.a_team.wakeupper.activities;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.project.a_team.wakeupper.R;
import com.project.a_team.wakeupper.UnblockActivity;

import java.util.Random;




public class PhraseActivity extends Activity {

    private TextView instructTextView;
    private EditText phraseEditText;
    static final Random random = new Random();
    private String phrase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrase);
        instructTextView = (TextView) findViewById(R.id.textView0);
        TextView ShowPhraseTextView = (TextView) findViewById(R.id.textView1);
        phraseEditText = (EditText) findViewById(R.id.editText0);
        phrase = generator();
        ShowPhraseTextView.setText(phrase);
        UnblockActivity.unlockScreen(this);
    }



    public void onClick(View view) {
        if (phraseEditText.getText().toString().equals(phrase)) {
            setResult(RESULT_OK);
            finish();
        }
        else {
            phraseEditText.getText().clear();
            instructTextView.setText(R.string.PhrActTextView02);
        }
    }

    private String generator() {
        Resources res = getResources();
        String[] array0 = res.getStringArray(R.array.array0);
        String[] array1 = res.getStringArray(R.array.array1);
        String[] array2 = res.getStringArray(R.array.array2);
        String[] array3 = res.getStringArray(R.array.array3);
        String[] array4 = res.getStringArray(R.array.array4);

        return array0[random.nextInt(array0.length)]+ " " + array1[random.nextInt(array1.length)] +
                " " + array2[random.nextInt(array2.length)] + " " +
                array3[random.nextInt(array3.length)] + " " + array4[random.nextInt(array4.length)];
    }

}

