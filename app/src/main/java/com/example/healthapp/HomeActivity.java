package com.example.healthapp;

import static android.content.ContentValues.TAG;
import android.Manifest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeActivity extends AppCompatActivity {

    AtomicInteger completedTasks = new AtomicInteger(0);
    TextView totalTextView;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                    Toast.makeText(this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
                }
            });

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
            }
            else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean themecheck = sharedPreferences.getBoolean("Light", false);
        if(themecheck)
        {
            setTheme(R.style.Base_Theme_HealthAppNight);
        }
        else {
            setTheme(R.style.Base_Theme_HealthApp);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        totalTextView = findViewById(R.id.totalText);
        askNotificationPermission();
        updateName();
        updateDate();
        updateData();
        checkNewNoti();
    }

    public void updateBox(String taskC, TextView textViewBox, AtomicInteger total, String todayDate) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        CollectionReference collectionReference = firestore.collection(userEmail);
        collectionReference.document(taskC).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    if (data != null) {
                        // Check if today's date exists as a field in the document
                        if (data.containsKey(todayDate)) {
                            // Retrieve the value associated with today's date
                            Object value = data.get(todayDate);
                            if (value != null) {
                                // Update the TextView with the value
                                runOnUiThread(() -> {
                                    textViewBox.setText(value.toString() + " Minutes");
                                    int intValue = Integer.parseInt(value.toString());
                                    total.addAndGet(intValue);
                                });
                            }
                        } else {
                            Log.d(TAG, "Today's date is not present in the document");
                        }
                    }
                } else {
                    Log.d(TAG, "Document 'taskC' does not exist");
                }
            } else {
                Log.d(TAG, "Failed to retrieve document 'Running'", task.getException());
            }
            checkTasksCompleted(total.get());
        });
    }

    public void updateData() {
        String todayDate = getCurrentDate();
        AtomicInteger total = new AtomicInteger();

        TextView runningText = findViewById(R.id.RunningText);
        TextView walkingText = findViewById(R.id.WalkingText);
        TextView weightText = findViewById(R.id.weightliftingText);
        TextView yogaText = findViewById(R.id.yogaText);
        TextView stairsText = findViewById(R.id.stairsText);

        updateBox("Running", runningText, total, todayDate);
        updateBox("Walking", walkingText, total, todayDate);
        updateBox("Weight Lifting", weightText, total, todayDate);
        updateBox("Yoga", yogaText, total, todayDate);
        updateBox("Stairs", stairsText, total, todayDate);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);

        return (Month + 1) + "-" + Day + "-" + Year;
    }

    private void updateTotalTextView(int total) {
        runOnUiThread(() -> totalTextView.setText(total + " Minutes"));
    }

    private void checkTasksCompleted(int total) {
        int completed = completedTasks.incrementAndGet();
        if (completed == 5) {
            updateTotalTextView(total);
        }
    }

    public void updateDate() {
        TextView dayTextView = findViewById(R.id.dayText);
        TextView monthTextView = findViewById(R.id.monthText);

        // Get the current date using Calendar
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);

        String[] months = new String[] {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        String currentMonthName = months[currentMonth];
        monthTextView.setText(currentMonthName);
        dayTextView.setText(String.valueOf(currentDay));
    }

    public void updateName() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        CollectionReference collectionReference = firestore.collection(userEmail);

        collectionReference.document("UserInfo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    Object value = data.get("Name");

                    String name = value != null ? String.valueOf(value) : "";
                    Log.d(TAG, "Name: " + name);
                    handleName(name);
                } else {
                    Log.d(TAG, "Error: Document doesn't exist");
                }
            } else {
                Log.d(TAG, "Error: Failed to retrieve document", task.getException());
            }
        });
    }

    private void handleName(String name) {
        TextView welcomeTextView = findViewById(R.id.WelcomeText);
        welcomeTextView.setText("Welcome " + name + "!");
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onClickNotifications(View view) {
        Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
        startActivity(intent);
    }

    public void onClickExercise(View view) {
        Intent intent = new Intent(HomeActivity.this, ExerciseActivity.class);
        startActivity(intent);
    }

    public void onClickHome(View view) {
    }


    public void onClickStat(View view) {
        Intent intent = new Intent(HomeActivity.this, StatsActivity.class);
        startActivity(intent);
    }

    public void checkNewNoti() {
        ImageButton notiIcon = findViewById(R.id.NotiIcon);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        CollectionReference collectionReference = firestore.collection(userEmail);

        collectionReference.document("UserInfo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    Object value = data.get("newNoti");

                    if (value != null && value instanceof Boolean) {
                        Boolean newNoti = (Boolean) value;

                        if (newNoti) {
                            notiIcon.setImageResource(R.drawable.bell_with_noti);
                        } else {
                            notiIcon.setImageResource(R.drawable.bell_solid_circle);
                        }
                    } else {
                        notiIcon.setImageResource(R.drawable.bell_solid_circle);
                    }
                } else {
                    Log.d(TAG, "Error: Document doesn't exist");
                }
            } else {
                Log.d(TAG, "Error: Failed to retrieve document", task.getException());
            }
        });
    }

}