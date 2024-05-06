
package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsActivity extends AppCompatActivity {

    TextView runningTime;
    TextView weightTime;
    TextView yogaTime;
    TextView walkingTime;
    TextView stairsTime;
    HashMap<String, String> runningData = new HashMap<>();
    HashMap<String, String> walkingData = new HashMap<>();
    HashMap<String, String> stairsData = new HashMap<>();
    HashMap<String, String> weightsData = new HashMap<>();
    HashMap<String, String> yogaData = new HashMap<>();


    private static final String TAG = "StatsActivity";
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        stairsTime = findViewById(R.id.StairsText);
        walkingTime = findViewById(R.id.WalkingText);
        yogaTime = findViewById(R.id.YogaText);
        weightTime = findViewById(R.id.WeightText);
        runningTime = findViewById(R.id.RunningText);

        LinearLayout WeekLayout = findViewById(R.id.Week);
        LinearLayout MonthLayout = findViewById(R.id.Month);
        LinearLayout YearLayout = findViewById(R.id.Year);



        DataEqual("Running");
        DataEqual("Yoga");
        DataEqual("Weight Lifting");
        DataEqual("Walking");
        DataEqual("Stairs");

        updateExerciseStats(7);
        WeekLayout.setBackgroundResource(R.drawable.statclickoutline);



        WeekLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExerciseStats(7);
                WeekLayout.setBackgroundResource(R.drawable.statclickoutline);
                MonthLayout.setBackgroundResource(R.drawable.border);
                YearLayout.setBackgroundResource(R.drawable.border);
            }
        });

        MonthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExerciseStats(30);
                WeekLayout.setBackgroundResource(R.drawable.border);
                MonthLayout.setBackgroundResource(R.drawable.statclickoutline);
                YearLayout.setBackgroundResource(R.drawable.border);
            }
        });

        YearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateExerciseStats(365);
                WeekLayout.setBackgroundResource(R.drawable.border);
                MonthLayout.setBackgroundResource(R.drawable.border);
                YearLayout.setBackgroundResource(R.drawable.statclickoutline);
            }
        });
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

    public interface DataCallback {
        void onDataLoaded(HashMap<String, String> data);
    }

    public void getALLData(String Type, DataCallback callback) {
        final HashMap<String, String> fieldValueMap = new HashMap<>(); // Initialize HashMap to store field names and values

        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();

        firestore.collection(userEmail).document(Type)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Iterate through all fields in the document
                                for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                                    String fieldName = entry.getKey();
                                    String fieldValue = entry.getValue().toString(); // Assuming all values are strings

                                    // Add field name and value to the HashMap
                                    fieldValueMap.put(fieldName, fieldValue);
                                }
                                // Call the callback method with the data
                                callback.onDataLoaded(fieldValueMap);
                            } else {
                                Log.d(TAG, "No document");
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }



    private void calculateTotalMinutes(HashMap<String, String> data,TextView textView, int time) {


        int totalMinutes = 0;
        for(int i=0; i<time; i++)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            int Year = calendar.get(Calendar.YEAR);
            int Month = calendar.get(Calendar.MONTH);
            int Day = calendar.get(Calendar.DAY_OF_MONTH);

            String selectedDate = (Month + 1) + "-" + Day + "-" + Year;
            for (Map.Entry<String, String> entry : data.entrySet()) {
                // Check if the entry's key (date) matches the selected date
                if (entry.getKey().equals(selectedDate)) {
                    // If the date matches, add the minutes to the total
                    totalMinutes += Integer.parseInt(entry.getValue());
                }
            }
        }

        textView.setText(String.valueOf(totalMinutes));
    }

    private void updateExerciseStats(int time) {
        calculateTotalMinutes(runningData, runningTime, time);
        calculateTotalMinutes(yogaData, yogaTime, time);
        calculateTotalMinutes(walkingData, walkingTime, time);
        calculateTotalMinutes(weightsData, weightTime, time);
        calculateTotalMinutes(stairsData, stairsTime, time);
    }
    private void DataEqual(String exerciseType) {
        getALLData(exerciseType, new DataCallback() {
            @Override
            public void onDataLoaded(HashMap<String, String> data) {
                if (exerciseType.equals("Running")) {
                    runningData = data;
                } else if (exerciseType.equals("Walking")) {
                    walkingData = data;
                } else if (exerciseType.equals("Stairs")) {
                    stairsData = data;
                } else if (exerciseType.equals("Weight Lifting")) {
                    weightsData = data;
                } else if (exerciseType.equals("Yoga")) {
                    yogaData = data;
                }
            }
        });
    }

    public void updateTotal()
    {



    }


}