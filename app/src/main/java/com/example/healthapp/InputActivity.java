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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Intent intent = getIntent();

        // Gets the type of Exercise From Last Screen
        ExerciseType = intent.getStringExtra("ExerciseType");

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

        durationText = findViewById(R.id.time);
        String durationStr = durationText.getText().toString();
        int duration = Integer.parseInt(durationStr);


        //The Date in millis secs
        long selectedDateInMillis = Calendar.getDate();

        Date selectedDate = new Date(selectedDateInMillis);

        // Format the Date object into a desired string format
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy"); // You can adjust the format as needed
        String formattedDate = sdf.format(selectedDate);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();


//        firestore.collection(userEmail).document(ExerciseType)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        // Check if the document exists
//                        if (task.getResult().exists()) {
//
//
//                        }
//                    } else {
//                        // Handle errors
//                        Exception exception = task.getException();
//                        if (exception != null) {
//                            // Handle exceptions
//                        }
//                    }
//                });


        firestore.collection(userEmail).document(ExerciseType)
                .update(formattedDate, duration)
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