package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);


        LinearLayout Running = findViewById(R.id.Running);
        LinearLayout Swimming = findViewById(R.id.Swimming);
        LinearLayout Yoga = findViewById(R.id.Yoga);
        LinearLayout Weights = findViewById(R.id.Weights);
        LinearLayout Stairs = findViewById(R.id.Stairs);
        Running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, InputActivity.class);
                intent.putExtra("ExerciseType", "Running");
                startActivity(intent);
            }
        });

        Swimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, InputActivity.class);
                intent.putExtra("ExerciseType", "Swimming");
                startActivity(intent);
            }
        });

        Yoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, InputActivity.class);
                intent.putExtra("ExerciseType", "Yoga");
                startActivity(intent);
            }
        });

        Stairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, InputActivity.class);
                intent.putExtra("ExerciseType", "Stairs");
                startActivity(intent);
            }
        });

        Weights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, InputActivity.class);
                intent.putExtra("ExerciseType", "Weight Lifting");
                startActivity(intent);
            }
        });
    }

    public void onClickHome(View view) {
        Intent intent = new Intent(ExerciseActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void onClickExercise(View view) {
    }

    public void onClickStat(View view) {
    }


}