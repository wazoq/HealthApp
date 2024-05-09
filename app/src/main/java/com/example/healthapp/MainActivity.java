package com.example.healthapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        boolean themecheck = sharedPreferences.getBoolean("Light", false);
        if(themecheck)
        {
            setTheme(R.style.Base_Theme_HealthApp);
        }
        else {
            setTheme(R.style.Base_Theme_HealthAppNight);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {

        super.onStart();
        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

//        Intent intent = new Intent(MainActivity.this, HomeActivity.class); // Change SignUpActivity to HomeActivity
//        startActivity(intent);


        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            if(Objects.equals(currentUser.getEmail(), "manager@gmail.com"))
            {
                Intent intent = new Intent(MainActivity.this, ManagerHomeActivity.class); // Change SignUpActivity to HomeActivity
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class); // Change SignUpActivity to HomeActivity
                startActivity(intent);
            }
        }
    }

    public void onClickLogin(View view) {
        // Redirect to the login page
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

}