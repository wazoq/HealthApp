//Lets user make an account and sets up a database collection in their email

package com.example.healthapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;

    Button signButton;

    EditText nameEditText;

    FirebaseAuth mAuth;

    FirebaseFirestore firestore;

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
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize EditText fields
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        nameEditText = findViewById(R.id.Name);
        signButton = findViewById(R.id.registerButton);


        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password,name;
                String usernameCap = String.valueOf(usernameEditText.getText());
                username = usernameCap.toLowerCase();
                password = String.valueOf(passwordEditText.getText());
                name = String.valueOf(nameEditText.getText());

                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(SignUpActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignUpActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(SignUpActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.length() > 12) {
                    Toast.makeText(SignUpActivity.this, "Name cannot be longer than 12 letters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //first it creates a authenticated user in the base
                mAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Account Made.",
                                            Toast.LENGTH_SHORT).show();


                                    //sets up the firestore for each of the type of Exercises aswell as Notifications
                                    firestore.collection(username).document("Yoga").set(new HashMap<>());
                                    firestore.collection(username).document("Running").set(new HashMap<>());
                                    firestore.collection(username).document("Walking").set(new HashMap<>());
                                    firestore.collection(username).document("Stairs").set(new HashMap<>());
                                    firestore.collection(username).document("Weight Lifting").set(new HashMap<>());
                                    firestore.collection(username).document("Notis").set(new HashMap<>());

                                    //Adds email to a collectoin of emails for the manager to see
                                    HashMap<String, Object> updateData = new HashMap<>();
                                    updateData.put(username, username);

                                    firestore.collection("JustEmails").document("Emails")
                                            .set(updateData, SetOptions.merge());



                                    Intent intent = new Intent(     SignUpActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });











                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (task.isSuccessful()) {
                                    // Get new FCM registration token
                                    String token = task.getResult();

                                    // Log and toast
                                    Log.d(TAG, "FCM Token: " + token);

                                    // Store the token in Firestore along with other user information
                                    HashMap<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("Name", name);
                                    userInfo.put("Token", token);
                                    firestore.collection(username).document("UserInfo").set(userInfo);

                                    // Proceed with other Firestore operations or navigate to the next screen
                                } else {
                                    // Handle token retrieval failure
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Failed to get FCM token",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


















            }
        });


    }

    public void onClickBack(View view) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
