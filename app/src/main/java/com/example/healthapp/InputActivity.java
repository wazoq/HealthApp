package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.Locale;

public class InputActivity extends AppCompatActivity {


    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent intent = getIntent();

        // Gets the type of Exercise From Last Screen
        String ExerciseType = intent.getStringExtra("ExerciseType");

        TextView Title = findViewById(R.id.exerciseType);
        Title.setText(ExerciseType);

        CalendarView Calendar =  findViewById(R.id.calendarView);

    }

    public void onClickBack(View view) {
        Intent intent = new Intent(InputActivity.this, ExerciseActivity.class);
        startActivity(intent);
    }

    public void onClickSubmit(View view) {
        firestore = FirebaseFirestore.getInstance();
        CalendarView Calendar =  findViewById(R.id.calendarView);

        //The Date in millis secs
        long selectedDateInMillis = Calendar.getDate();

        Date selectedDate = new Date(selectedDateInMillis);

        // Format the Date object into a desired string format
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // You can adjust the format as needed
        String formattedDate = sdf.format(selectedDate);

    }




}