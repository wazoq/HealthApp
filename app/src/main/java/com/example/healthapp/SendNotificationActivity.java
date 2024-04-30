package com.example.healthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SendNotificationActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_notification);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(SendNotificationActivity.this, ManagerHomeActivity.class);
        startActivity(intent);
    }

    String[] emailArray = {"ali@gmail.com", "aliranjha703@gmail.com", "hamza2@gmail.com"};



//    public void onClickSend(View view) {
//        firestore = FirebaseFirestore.getInstance();
//        EditText messageEditText = findViewById(R.id.message);
//        String messageText = messageEditText.getText().toString();
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        //String userEmail = user.getEmail();
//
//        for (int i = 0; i < emailArray.length; i++) {
//            String userEmail = emailArray[i];
//            String temp = "noti" + i;
//            firestore.collection(userEmail).document("Notis")
//                    .update(temp, messageText)
//                    .addOnSuccessListener(aVoid -> {
//                        // Document successfully updated
//                        // Handle success if needed
//                        Toast.makeText(SendNotificationActivity.this, "Noti Sent.",
//                                Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle errors
//                        if (e != null) {
//                            // Handle exceptions
//                            Toast.makeText(SendNotificationActivity.this, "Noti Failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }


//    public void onClickSend(View view) {
//        firestore = FirebaseFirestore.getInstance();
//        EditText messageEditText = findViewById(R.id.message);
//        String messageText = messageEditText.getText().toString();
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        // String userEmail = user.getEmail();
//
//        for (String userEmail : emailArray) {
//            firestore.collection(userEmail).document("Notis")
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            int fieldCount = documentSnapshot.getData().size();
//                            String temp = "noti" + (fieldCount + 1);
//                            firestore.collection(userEmail).document("Notis")
//                                    .update(temp, messageText)
//                                    .addOnSuccessListener(aVoid -> {
//                                        // Document successfully updated
//                                        // Handle success if needed
//                                        Toast.makeText(SendNotificationActivity.this, "Noti Sent.",
//                                                Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // Handle errors
//                                        if (e != null) {
//                                            // Handle exceptions
//                                            Toast.makeText(SendNotificationActivity.this, "Noti Failed.",
//                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                        } else {
//                            // Document does not exist, handle accordingly
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle errors
//                        if (e != null) {
//                            // Handle exceptions
//                            Toast.makeText(SendNotificationActivity.this, "Failed to get document: " + e.getMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }


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
                            Map<String, Object> data = documentSnapshot.getData();
                            int highestNumber = 0;
                            for (String key : data.keySet()) {
                                if (key.startsWith("noti")) {
                                    String numberStr = key.substring(4); // Remove "noti" prefix
                                    int number = Integer.parseInt(numberStr);
                                    if (number > highestNumber) {
                                        highestNumber = number;
                                    }
                                }
                            }
                            String temp = "noti" + (highestNumber + 1);
                            firestore.collection(userEmail).document("Notis")
                                    .update(temp, messageText)
                                    .addOnSuccessListener(aVoid -> {
                                        // Document successfully updated
                                        // Handle success if needed
                                        Toast.makeText(SendNotificationActivity.this, "Noti Sent.",
                                                Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle errors
                                        if (e != null) {
                                            // Handle exceptions
                                            Toast.makeText(SendNotificationActivity.this, "Noti Failed.",
                                                    Toast.LENGTH_SHORT).show();
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
    }




}

