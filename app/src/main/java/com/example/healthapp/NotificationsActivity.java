//Gets all the notis from the collectoin called Notis and Displays them to the User
package com.example.healthapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class NotificationsActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    String[] emailArray = {};

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
        setContentView(R.layout.activity_notifications);
        onClickUpdate();
    }

    public void onClickUpdate() {
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        ArrayList<String> allNotis = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection(userEmail);

        collectionReference.document("Notis").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    for (String key : data.keySet()) {
                        Object value = data.get(key);
                        String date = key;
                        String valueString = String.valueOf(value);
                        allNotis.add(valueString);
                        allNotis.add(date);
                        Log.d(TAG, "Field: " + key + ", Value: " + value);
                    }
                    updateNotificationTextView(allNotis);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

        firestore.collection(userEmail).document("UserInfo")
                .update("newNoti", false)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    if (e != null) {
                    }
                });
    }

    //Showing the Notis on the screen to the user
    private void updateNotificationTextView(ArrayList<String> allNotis) {
        // Find the parent LinearLayout
        LinearLayout parentLayout = findViewById(R.id.notificationsLayout);

        // Clear existing views before adding new ones
        parentLayout.removeAllViews();

        for (int i = 0; i < allNotis.size(); i += 2) {
            String fieldName = allNotis.get(i);
            String fieldValue = allNotis.get(i + 1);

            String fieldText = fieldValue + "\n" + fieldName;

            // Create a new TextView for the field name and value
            TextView fieldTextView = new TextView(this);
            fieldTextView.setText(fieldText);
            fieldTextView.setTextSize(18);
            fieldTextView.setPadding(50, 50, 50, 50);
            fieldTextView.setTextColor(getResources().getColor(android.R.color.white));
            fieldTextView.setBackgroundResource(R.drawable.rounded_corners_background);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(50, 40, 50,  40);
            fieldTextView.setLayoutParams(params);
            fieldTextView.setOnTouchListener(new OnSwipeTouchListener(this) {
                public void onSwipeLeft() {
                    showDeleteConfirmationDialog(fieldValue);
                }
            });
            parentLayout.addView(fieldTextView);
        }
    }

    private void deleteNotification(String notification) {
        // Delete the notification from Firebase
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        CollectionReference collectionReference = firestore.collection(userEmail);

        collectionReference.document("Notis").update(notification, FieldValue.delete())
                .addOnSuccessListener(aVoid -> {
                    // Notification deleted successfully
                    Log.d(TAG, "Notification deleted successfully");
                    onClickUpdate();
                })
                .addOnFailureListener(e -> {
                    // Failed to delete the notification
                    Log.e(TAG, "Error deleting notification", e);
                    // Show an error message
                    Toast.makeText(this, "Failed to delete notification", Toast.LENGTH_SHORT).show();
                });
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(NotificationsActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    //Confirmation for deleteing a noti
    private void showDeleteConfirmationDialog(String fieldValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");

        // Delete Message
        builder.setMessage("Are you sure you want to delete this?");

        // Buttons on Display
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNotification(fieldValue);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}