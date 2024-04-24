package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManagerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_home);

        LinearLayout sendNotificationBox = findViewById(R.id.send_notification_box);
        sendNotificationBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the login page
                Intent intent = new Intent(ManagerHomeActivity.this, SendNotificationActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout particapantInfoBox = findViewById(R.id.participant_information);
        particapantInfoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the login page
                Intent intent = new Intent(ManagerHomeActivity.this, SendNotificationActivity.class);
                startActivity(intent);
            }
        });


    }




}