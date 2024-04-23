package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent intent = getIntent();

        // Gets the type of Exercise From Last Screen
        String ExerciseType = intent.getStringExtra("ExerciseType");

        TextView Title = findViewById(R.id.exerciseType);
        Title.setText(ExerciseType);


        NumberPicker monthPicker = findViewById(R.id.monthPicker);
        final String[] months = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(months.length - 1);
        monthPicker.setDisplayedValues(months);
        monthPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        monthPicker.setWrapSelectorWheel(false);


    }

    public void onClickBack(View view) {
        Intent intent = new Intent(InputActivity.this, ExerciseActivity.class);
        startActivity(intent);
    }



}