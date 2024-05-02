package com.example.healthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







public class SendNotificationActivity extends AppCompatActivity {

    FirebaseFirestore firestore;


    String[] emailArray = {"account@gmail.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
                            String temp = "noti" + (fieldCount + 1);
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

