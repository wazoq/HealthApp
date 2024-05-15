package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    String[] emailArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        getEmails();
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(SendNotificationActivity.this, ManagerHomeActivity.class);
        startActivity(intent);
    }

    public void getEmails() {
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("JustEmails").document("Emails")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Collection<Object> emailValues = documentSnapshot.getData().values();
                        List<String> emailsList = new ArrayList<>();

                        for (Object value : emailValues) {
                            if (value instanceof String) {
                                String email = (String) value;
                                if (!email.isEmpty()) {
                                    emailsList.add(email);
                                }
                            }
                        }
                        // Convert list to array
                        emailArray = emailsList.toArray(new String[0]);
                    } else {
                        Log.d("SendNotificationActivity", "Emails document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("SendNotificationActivity", "Failed to get emails: " + e.getMessage());
                    }
                });
    }

    public void onClickSend(View view) {
        firestore = FirebaseFirestore.getInstance();
        EditText messageEditText = findViewById(R.id.message);
        String messageText = messageEditText.getText().toString();
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
        String formattedDate = sdf.format(currentDate);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        for (String userEmail : emailArray) {
            firestore.collection(userEmail).document("Notis")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int fieldCount = documentSnapshot.getData().size();
                            firestore.collection(userEmail).document("Notis")
                                    .update(formattedDate, messageText)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SendNotificationActivity.this, "Notification Sent.",
                                                Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        if (e != null) {
                                            Toast.makeText(SendNotificationActivity.this, "Notification Failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            firestore.collection(userEmail).document("UserInfo")
                                    .update("newNoti", true)
                                    .addOnSuccessListener(aVoid -> {
                                    })
                                    .addOnFailureListener(e -> {
                                        if (e != null) {
                                            Toast.makeText(SendNotificationActivity.this, "newNoti boolean failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SendNotificationActivity.this, "Document does not exist.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (e != null) {
                            Toast.makeText(SendNotificationActivity.this, "Failed to get document: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                formattedDate,
                messageText, getApplicationContext(), SendNotificationActivity.this);
        notificationsSender.SendNotifications();
        messageEditText.setText("");
    }
}
