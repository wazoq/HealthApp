package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
    }

    public void onClickExercise(View view) {
        Intent intent = new Intent(StatsActivity.this, ExerciseActivity.class);
        startActivity(intent);
    }

    public void onClickHome(View view) {
        Intent intent = new Intent(StatsActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    public void onClickStat(View view) {

    }

}