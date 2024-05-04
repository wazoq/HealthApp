package com.example.healthapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HomeActivity extends AppCompatActivity {

//    TextView quoteBox = findViewById(R.id.quote);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        updateName();
        updateDate();
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
        welcomeTextView.setText("Welcome " + name);
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

}