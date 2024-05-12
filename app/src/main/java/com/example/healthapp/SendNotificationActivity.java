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






import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;












public class SendNotificationActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {

                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
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


    FirebaseFirestore firestore;
    String[] emailArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        getEmails();
        askNotificationPermission();




















        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String usertoken = task.getResult();
                            Log.d("tooooo", "token: " + usertoken);
                        } else {
                            Log.e("Error", "Fetching FCM registration token failed", task.getException());
                        }
                    }
                });





        EditText title = findViewById(R.id.titleId);
        EditText message = findViewById(R.id.messageId);
        EditText token = findViewById(R.id.tokenId);

        findViewById(R.id.alldeviceId).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                            title.getText().toString(),
                            message.getText().toString(), getApplicationContext(), SendNotificationActivity.this);
                    notificationsSender.SendNotifications();

                }
                else {
                    Toast.makeText(SendNotificationActivity.this, "Write some text", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.singledeviceId).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty()
                        && !token.getText().toString().isEmpty()) {

                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token.getText().toString(),
                            title.getText().toString(),
                            message.getText().toString(), getApplicationContext(), SendNotificationActivity.this);
                    notificationsSender.SendNotifications();
                }
                else {
                    Toast.makeText(SendNotificationActivity.this, "Enter token", Toast.LENGTH_LONG).show();
                }
            }

        });

























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

        //sendNotificationToTopic(messageText);





    }









//    private void sendNotificationToTopic(String messageText) {
//        // Construct the notification message
//        Map<String, String> data = new HashMap<>();
//        data.put("message", messageText);
//
//        // Send the notification to the topic
//        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("pushNotis")
//                .setData(data)
//                .build());
//    }



}

