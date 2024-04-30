package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No authenticated user found", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = user.getEmail();
        firestore = FirebaseFirestore.getInstance();
        durationText = findViewById(R.id.time);
        String durationStr = durationText.getText().toString();

        try {
            int duration = Integer.parseInt(durationStr);
            firestore.collection(userEmail).document(ExerciseType)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        int temp = duration;
                        if (documentSnapshot.exists()) {
                            Object selectedDateObject = documentSnapshot.get(selectedDate);
                            if (selectedDateObject != null) {
                                int currentDuration = ((Long) selectedDateObject).intValue();
                                temp += currentDuration;
                            }
                        }
                        firestore.collection(userEmail).document(ExerciseType)
                                .update(selectedDate, temp)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Duration updated successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to update duration", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to retrieve document", Toast.LENGTH_SHORT).show();
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }


}