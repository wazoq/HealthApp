package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;


public class StatsActivity extends AppCompatActivity {


    TextView runningTime;
    TextView weightTime;
    TextView yogaTime;
    TextView walkingTime;
    TextView stairsTime;


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
        WeekLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   runningTime.setText(getData(7, "Running"));
//                walkingTime.setText(getData(7,"Waling"));
                yogaTime.setText(getData(7,"Yoga"));
//                stairsTime.setText(getData(7,"Stairs"));
//                weightTime.setText(getData(7,"Weight Lifting"));
            }
        });

        MonthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                runningTime.setText(getData(30,"Running"));
//                walkingTime.setText(getData(30,"Waling"));
//                yogaTime.setText(getData(30,"Yoga"));
//                stairsTime.setText(getData(30,"Stairs"));
//                weightTime.setText(getData(30,"Weight Lifting"));
            }
        });

        YearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                runningTime.setText(getData(365,"Running"));
//                walkingTime.setText(getData(365,"Waling"));
//                yogaTime.setText(getData(365,"Yoga"));
//                stairsTime.setText(getData(365,"Stairs"));
//                weightTime.setText(getData(365,"Weight Lifting"));
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


//    public int getData(int time, String Type) {
//
//        firestore = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userEmail = user.getEmail();
//
//        Calendar calendar = Calendar.getInstance();
//        int curYear = calendar.get(Calendar.YEAR);
//        int curMonth = calendar.get(Calendar.MONTH);
//        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//        String selectedDate = (curMonth + 1) + "-" + curDay + "-" + curYear;
//
//
//        List<Integer> totals = new ArrayList();
//
//        for (int i = 0; i < time; i++) {
//
//            calendar.add(Calendar.DAY_OF_MONTH, -i);
//            int checkYear = calendar.get(Calendar.YEAR);
//            int checkMonth = calendar.get(Calendar.MONTH);
//            int checkDay = calendar.get(Calendar.DAY_OF_MONTH);
//            String checkDate = (checkMonth + 1) + "-" + checkDay + "-" + checkYear;
//
//            firestore.collection(userEmail).document(Type)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    // Get the value of the "checkDate" field
//                                    String fieldValue = document.getString("checkDate");
//                                    if (fieldValue != null) {
//                                        Log.d(TAG, "Value of checkDate field: " + fieldValue);
//                                        totals.add(Integer.parseInt(fieldValue));
//                                    } else {
//                                        Log.d(TAG, "checkDate field does not exist or is null");
//                                    }
//                                } else {
//                                    Log.d(TAG, "No document");
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting document: ", task.getException());
//                            }
//                        }
//                    });
//
//
//        }
//
//        int timetotal = 0;
//        for (int num : totals) {
//            timetotal += num;
//        }
//
//        return timetotal;
//
//    }


        public int getData(int time, String Type) {

            firestore = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userEmail = user.getEmail();

            Calendar calendar = Calendar.getInstance();
            int curYear = calendar.get(Calendar.YEAR);
            int curMonth = calendar.get(Calendar.MONTH);
            int curDay = calendar.get(Calendar.DAY_OF_MONTH);

            String selectedDate = (curMonth + 1) + "-" + curDay + "-" + curYear;
            List<Integer> totals = new ArrayList<>();

//            firestore.collection(userEmail).document(Type)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                DocumentSnapshot document = task.getResult();
//                                if (document.exists()) {
//                                    // Initialize HashMap to store field names and values
//                                    HashMap<String, String> fieldValueMap = new HashMap<>();
//
//                                    // Iterate through all fields in the document
//                                    for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
//                                        String fieldName = entry.getKey();
//                                        String fieldValue = entry.getValue().toString(); // Assuming all values are strings
//
//                                        // Add field name and value to the HashMap
//                                        fieldValueMap.put(fieldName, fieldValue);
//                                    }
//                                } else {
//                                    Log.d(TAG, "No document");
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting document: ", task.getException());
//                            }
//                        }
//                    });



            return 100000;
        }



}