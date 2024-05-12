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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;



// create a new field in notis called read, everytime you add noti, read = false
// and everytime notis page open, read = true

public class HomeActivity extends AppCompatActivity {

//    TextView quoteBox = findViewById(R.id.quote);
//test
// Define a counter for completed tasks
    AtomicInteger completedTasks = new AtomicInteger(0);

    // Define the total TextView
    TextView totalTextView;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {


                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
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
            setTheme(R.style.Base_Theme_HealthApp);
        }
        else {
            setTheme(R.style.Base_Theme_HealthAppNight);
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

        // Get document "Running" from the collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        CollectionReference collectionReference = firestore.collection(userEmail);
        collectionReference.document(taskC).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot contains the data of the document
                    Map<String, Object> data = document.getData();
                    if (data != null) {
                        // Check if today's date exists as a field in the document
                        if (data.containsKey(todayDate)) {
                            // Retrieve the value associated with today's date
                            Object value = data.get(todayDate);
                            // Handle the value as needed
                            // For example:
                            if (value != null) {
                                // Update the TextView with the value
                                runOnUiThread(() -> {
                                    textViewBox.setText(value.toString() + " Minutes");
                                    int intValue = Integer.parseInt(value.toString());
                                    total.addAndGet(intValue);
                                });
                            }
                        } else {
                            // Today's date is not present in the document
                            // Log.d(TAG, "Today's date is not present in the document");
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
        // Get today's date in the format "MM-DD-YYYY"
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
//        // Get today's date
//        Date today = Calendar.getInstance().getTime();
//        // Format the date as "MM-DD-YYYY"
//        SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy", Locale.getDefault());
//        return dateFormat.format(today);
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);

        return (Month + 1) + "-" + Day + "-" + Year;
    }

    // test
    private void updateTotalTextView(int total) {
        runOnUiThread(() -> totalTextView.setText(total + " Minutes"));
    }

    private void checkTasksCompleted(int total) {
        int completed = completedTasks.incrementAndGet();
        if (completed == 5) { // Assuming there are 6 asynchronous tasks
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

        // Get document from collection
        collectionReference.document("UserInfo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot contains the data of the document
                    Map<String, Object> data = document.getData();
                    // Now you can use the 'data' map to access all fields and their values
                    Object value = data.get("Name");

                    String name = value != null ? String.valueOf(value) : "";
                    // Do something with the retrieved name value, such as displaying it
                    Log.d(TAG, "Name: " + name);

                    // Call a method to handle the retrieved name value
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
        // Here, you can use the retrieved name value as needed
        // For example, you can update a TextView with the name
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

        // Get document from collection
        collectionReference.document("UserInfo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot contains the data of the document
                    Map<String, Object> data = document.getData();
                    // Now you can use the 'data' map to access all fields and their values
                    Object value = data.get("newNoti");

                    if (value != null && value instanceof Boolean) {
                        // Cast the value to Boolean
                        Boolean newNoti = (Boolean) value;

                        // Now you can use the boolean value as needed
                        if (newNoti) {
                            // Handle the case when 'newNoti' is true
                            notiIcon.setImageResource(R.drawable.bell_with_noti);
                        } else {
                            // Handle the case when 'newNoti' is false
                            notiIcon.setImageResource(R.drawable.bell_solid_circle);
                        }
                    } else {
                        // Handle the case when the value is null or not of type Boolean
                        // For example, set a default image resource
                        notiIcon.setImageResource(R.drawable.bell_solid_circle);
                    }

                    // Call a method to handle the retrieved name value
                } else {
                    Log.d(TAG, "Error: Document doesn't exist");
                }
            } else {
                Log.d(TAG, "Error: Failed to retrieve document", task.getException());
            }
        });




    }





}