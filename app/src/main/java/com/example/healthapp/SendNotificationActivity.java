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
                        // Document exists, retrieve email addresses
                        Collection<Object> emailValues = documentSnapshot.getData().values();
                        List<String> emailsList = new ArrayList<>();

                        for (Object value : emailValues) {
                            if (value instanceof String) {
                                String email = (String) value;
                                if (!email.isEmpty()) {
                                    // Add non-empty email strings to the list
                                    emailsList.add(email);
                                }
                            }
                        }

                        // Convert list to array
                        emailArray = emailsList.toArray(new String[0]);
                    } else {
                        // Document does not exist, handle accordingly
                        Log.d("SendNotificationActivity", "Emails document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    if (e != null) {
                        // Handle exceptions
                        Log.e("SendNotificationActivity", "Failed to get emails: " + e.getMessage());
                    }
                });
    }






    public void onClickSend(View view) {
        firestore = FirebaseFirestore.getInstance();
        EditText messageEditText = findViewById(R.id.message);
        String messageText = messageEditText.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // String userEmail = user.getEmail();

        for (String userEmail : emailArray) {
            firestore.collection(userEmail).document("Notis")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            int fieldCount = documentSnapshot.getData().size();
                            //String temp = "noti" + (fieldCount + 1);
                            Date currentDate = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
                            String formattedDate = sdf.format(currentDate);
                            firestore.collection(userEmail).document("Notis")
                                    .update(formattedDate, messageText)
                                    .addOnSuccessListener(aVoid -> {
                                        // Document successfully updated
                                        // Handle success if needed

                                        //FCMSend.pushNotification();
                                        Toast.makeText(SendNotificationActivity.this, "Notification Sent.",
                                                Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle errors
                                        if (e != null) {
                                            // Handle exceptions
                                            Toast.makeText(SendNotificationActivity.this, "Notification Failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            firestore.collection(userEmail).document("UserInfo")
                                    .update("newNoti", true)
                                    .addOnSuccessListener(aVoid -> {
                                        // Document successfully updated
                                        // Handle success if needed

                                        //FCMSend.pushNotification();
                                        //Toast.makeText(SendNotificationActivity.this, "newNoti boolean added.",
                                        //        Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle errors
                                        if (e != null) {
                                            // Handle exceptions
                                          //  Toast.makeText(SendNotificationActivity.this, "newNoti boolean failed.",
                                          //          Toast.LENGTH_SHORT).show();
                                        }
                                    });





                        } else {
                            // Document does not exist, handle accordingly
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        if (e != null) {
                            // Handle exceptions
                            Toast.makeText(SendNotificationActivity.this, "Failed to get document: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }


//        // This registration token comes from the client FCM SDKs.
//        String registrationToken = "YOUR_REGISTRATION_TOKEN";
//
//        // See documentation on defining a message payload.
//                Message message = Message.builder()
//                        .putData("score", "850")
//                        .putData("time", "2:45")
//                        .setToken(registrationToken)
//                        .build();
//
//        // Send a message to the device corresponding to the provided
//        // registration token.
//                String response = FirebaseMessaging.getInstance().send(message);
//        // Response is a message ID string.
//        System.out.println("Successfully sent message: " + response);

        sendNotificationToTopic(messageText);
    }

    private void sendNotificationToTopic(String messageText) {
        // Construct the notification message
        Map<String, String> data = new HashMap<>();
        data.put("message", messageText);

        // Send the notification to the topic
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("pushNotis")
                .setData(data)
                .build());
    }



}

