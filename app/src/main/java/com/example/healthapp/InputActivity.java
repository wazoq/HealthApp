package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.Locale;

public class InputActivity extends AppCompatActivity {


    FirebaseFirestore firestore;
    String ExerciseType;
    EditText durationText;

    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent intent = getIntent();

        // Gets the type of Exercise From Last Screen
        ExerciseType = intent.getStringExtra("ExerciseType");

        TextView Title = findViewById(R.id.exerciseType);
        Title.setText(ExerciseType);


        CalendarView calendarView = findViewById(R.id.calendarView);

        // Set the OnDateChangeListener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Format the date with a separator for better readability
                selectedDate = (month + 1) + "-" + dayOfMonth + "-" + year;
                // Toast.makeText(InputActivity.this, "Exercise duration updated for " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void onClickBack(View view) {
        Intent intent = new Intent(InputActivity.this, ExerciseActivity.class);
        startActivity(intent);
    }

    public void onClickSubmit(View view) {
        firestore = FirebaseFirestore.getInstance();


        durationText = findViewById(R.id.time);
        String durationStr = durationText.getText().toString();
        int duration = Integer.parseInt(durationStr);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();




        firestore.collection(userEmail).document(ExerciseType)
                .update(selectedDate, duration)
                .addOnSuccessListener(aVoid -> {
                    // Document successfully updated
                    // Handle success if needed
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    if (e != null) {
                        // Handle exceptions
                    }
                });
    }




}