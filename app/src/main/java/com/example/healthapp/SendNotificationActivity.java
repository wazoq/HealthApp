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

    FirebaseFirestore firestore;
    String[] emailArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        getEmails();
        //askNotificationPermission();




















//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            String usertoken = task.getResult();
//                            Log.d("tooooo", "token: " + usertoken);
//                        } else {
//                            Log.e("Error", "Fetching FCM registration token failed", task.getException());
//                        }
//                    }
//                });





//        EditText title = findViewById(R.id.titleId);
//        EditText message = findViewById(R.id.messageId);
//        EditText token = findViewById(R.id.tokenId);
//
//        findViewById(R.id.alldeviceId).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {
//
//                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
//                            title.getText().toString(),
//                            message.getText().toString(), getApplicationContext(), SendNotificationActivity.this);
//                    notificationsSender.SendNotifications();
//
//                }
//                else {
//                    Toast.makeText(SendNotificationActivity.this, "Write some text", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

//        findViewById(R.id.singledeviceId).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty()
//                        && !token.getText().toString().isEmpty()) {
//
//                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token.getText().toString(),
//                            title.getText().toString(),
//                            message.getText().toString(), getApplicationContext(), SendNotificationActivity.this);
//                    notificationsSender.SendNotifications();
//                }
//                else {
//                    Toast.makeText(SendNotificationActivity.this, "Enter token", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        });

























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
                            //String temp = "noti" + (fieldCount + 1);
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

        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                formattedDate,
                messageText, getApplicationContext(), SendNotificationActivity.this);
        notificationsSender.SendNotifications();

        messageEditText.setText("");

    }

}

