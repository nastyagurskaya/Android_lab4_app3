package com.example.nastyagurskaya.lab4_app3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddMark extends AppCompatActivity  implements GestureOverlayView.OnGesturePerformedListener {

    GestureLibrary gLib;
    GestureOverlayView gestures;
    ImageButton imageButtonDeleteInput;
    TextView editText;
    boolean end = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mark_act);

        editText = (TextView)findViewById(R.id.textView);

        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        imageButtonDeleteInput = (ImageButton) findViewById(R.id.delImput);
        imageButtonDeleteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultExpr =editText.getText().toString();
                if (end) {
                    editText.setText("");
                    return;
                }
                if (resultExpr.length() != 0) {
                    editText.setText(resultExpr.substring(0, resultExpr.length() - 1));
                }
            }
        });

        imageButtonDeleteInput.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editText.setText("");
                return true;
            }
        });
        if (!gLib.load()) {
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.geust);
        gestures.addOnGesturePerformedListener(this);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        //Создаёт ArrayList c загруженными из gestures жестами
        ArrayList<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0) {
            //если загружен хотябы один жест из gestures
            String[] arr = {"a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n",
                    "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "D", "E", "F", "G", "H", "K",
                    "L", "M", "N"};
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                boolean flag = true;
                for(String str: arr) {
                    if (prediction.name.equals(str)) {
                        editText.setText(editText.getText() + str);
                        flag = false;
                        Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
                    }
                }
                if(flag) {
                    editText.setText(editText.getText() + " ");
                    Toast.makeText(getApplicationContext(),"Space",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),"Gestures wasn't founded",Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void addInPref(View v) {
        SharedPreferences countSettings = getSharedPreferences("text_set", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = countSettings.edit();
        e.putString("Text", countSettings.getString("Text", "") + editText.getText());
        e.commit();
        e.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
