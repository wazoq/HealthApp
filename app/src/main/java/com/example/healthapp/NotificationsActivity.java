package com.example.healthapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
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

public class NotificationsActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    String[] emailArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        onClickUpdate();
    }

    public void onClickUpdate() {
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        ArrayList<String> allNotis = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection(userEmail);

        // Get document from collection
        collectionReference.document("Notis").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot contains the data of the document
                    Map<String, Object> data = document.getData();
                    // Now you can use the 'data' map to access all fields and their values
                    for (String key : data.keySet()) {
                        Object value = data.get(key);

                        String date = key;

                        String valueString = String.valueOf(value);
//                        TextView textView = findViewById(R.id.noti1);
//                        textView.setText(valueString);
                        allNotis.add(valueString);
                        allNotis.add(date);
                        // Do something with each field and its value
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





    }

    private void updateNotificationTextView(ArrayList<String> allNotis) {
        // Find the parent LinearLayout where you want to add the TextViews
        LinearLayout parentLayout = findViewById(R.id.notificationsLayout); // Replace R.id.parentLayout with the ID of your parent LinearLayout

        // Clear existing views before adding new ones
        parentLayout.removeAllViews();

        // Iterate through each notification string
//        for (String notification : allNotis) {
//            // Create a new TextView
//            TextView textView = new TextView(this);
//
//            // Set the text of the TextView
//            textView.setText(notification);
//            textView.setTextSize(18);
//            textView.setPadding(50, 50, 50, 50);
//            textView.setTextColor(getResources().getColor(android.R.color.white));
//
//            // Set the background drawable resource with rounded corners
//            textView.setBackgroundResource(R.drawable.rounded_corners_background);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT // Use WRAP_CONTENT for TextView height
//            );
//            params.setMargins(50, 40, 50,  40); // Adjust margins as needed
//            textView.setLayoutParams(params);
//
//            textView.setOnLongClickListener(v -> {
//                deleteNotification(notification);
//                // Return true to indicate that the long click event is consumed
//                return true;
//            });
//
//            // Add the TextView to the parent layout
//            parentLayout.addView(textView);
//        }


        for (int i = 0; i < allNotis.size(); i += 2) {
            // Get the field name and value at the current index
            String fieldName = allNotis.get(i);
            String fieldValue = allNotis.get(i + 1);

            // Concatenate the field name and value with a newline character
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
                    LinearLayout.LayoutParams.WRAP_CONTENT // Use WRAP_CONTENT for TextView height
            );
            params.setMargins(50, 40, 50,  40); // Adjust margins as needed
            fieldTextView.setLayoutParams(params);
            fieldTextView.setOnTouchListener(new OnSwipeTouchListener(this) {
                public void onSwipeLeft() {
                    // Handle swipe left action (delete notification)
                    deleteNotification(fieldValue);
                }
                public void onSwipeRight() {
                    // Handle swipe left action (delete notification)
                    //deleteNotification(fieldValue);
                }
            });

            // Add the TextView to the parent layout
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
                    // Update the UI to reflect the changes (reload the notifications)
                    onClickUpdate();
                })
                .addOnFailureListener(e -> {
                    // Failed to delete the notification
                    Log.e(TAG, "Error deleting notification", e);
                    // Show an error message to the user
                    Toast.makeText(this, "Failed to delete notification", Toast.LENGTH_SHORT).show();
                });
    }



    public void onClickBack(View view) {
        Intent intent = new Intent(NotificationsActivity.this, HomeActivity.class);
        startActivity(intent);
    }



}