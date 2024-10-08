package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

public class ManagerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

        LinearLayout sendNotificationBox = findViewById(R.id.send_notification_box);
        sendNotificationBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the send notifications page
                Intent intent = new Intent(ManagerHomeActivity.this, SendNotificationActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout particapantInfoBox = findViewById(R.id.participant_information);
        particapantInfoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the participant info page
                Intent intent = new Intent(ManagerHomeActivity.this, ManagerSelectionActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout changePassword = findViewById(R.id.change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });
    }

    private void sendPasswordResetEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("SendPasswordReset", "Password reset email sent.");
                                Toast.makeText(ManagerHomeActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("SendPasswordReset", "Failed to send password reset email.", task.getException());
                                Toast.makeText(ManagerHomeActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}