package com.example.healthapp;

import android.app.Activity;
import android.content.Intent;
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
                username = String.valueOf(usernameEditText.getText());
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

                mAuth.createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Account Made.",
                                            Toast.LENGTH_SHORT).show();


                                    firestore.collection(username).document("Yoga").set(new HashMap<>());
                                    firestore.collection(username).document("Running").set(new HashMap<>());
                                    firestore.collection(username).document("Walking").set(new HashMap<>());
                                    firestore.collection(username).document("Stairs").set(new HashMap<>());
                                    firestore.collection(username).document("Weight Lifting").set(new HashMap<>());
                                    firestore.collection(username).document("Notis").set(new HashMap<>());

                                    HashMap<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("Name", name);
                                    firestore.collection(username).document("UserInfo").set(userInfo);

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




            }
        });

        // Implement your authentication logic here
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
