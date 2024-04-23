package com.example.healthapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.google.firebase.auth.FirebaseAuth;

//import { getAuth, sendPasswordResetEmail } from "firebase/auth";

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Boolean darkMode;
    Boolean notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch darkModeSwitch = findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    darkMode = true;
                } else {
                    darkMode = false;
                }
            }
        });

        Switch notificationsSwitch = findViewById(R.id.notifications_switch);
        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notifications = true;
                } else {
                    notifications = false;
                }
            }
        });

        LinearLayout changePassword = findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send firebase link to reset password
            }
        });

    }
}

