//package com.example.healthapp;
//
//import static android.content.ContentValues.TAG;
//
//import android.annotation.SuppressLint;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.InputType;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
////import { getAuth, sendPasswordResetEmail } from "firebase/auth";
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class SettingsActivity extends AppCompatActivity {
//
//    Boolean darkMode;
//    Boolean notifications;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//
//        Switch darkModeSwitch = findViewById(R.id.darkModeSwitch);
//        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    darkMode = true;
//                } else {
//                    darkMode = false;
//                }
//            }
//        });
//
//
//        Switch notificationsSwitch = findViewById(R.id.notifications_switch);
//        notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    notifications = true;
//                } else {
//                    notifications = false;
//                }
//            }
//        });
//
//        LinearLayout changePassword = findViewById(R.id.change_password);
//        changePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // send firebase link to reset password
//                sendPasswordResetEmail();
//            }
//        });
//
//        LinearLayout logoutBtn = findViewById(R.id.logout);
//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                // After logout, clear all activities and start the LoginActivity
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish(); // Call finish to close the current activity
//            }
//        });
//
//        LinearLayout changeName = findViewById(R.id.change_name);
//        changeName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //showChangeNameDialog();
//                showCustomPopup(v);
//            }
//        });
//        View view = null;
//        updateScreenName(view);
////        View view = null;
////        showCustomPopup(view);
//    }
//
//
//    public void showCustomPopup(View anchorView) {
//        // Inflate the custom layout
//        View customView = getLayoutInflater().inflate(R.layout.custom_popup_layout, null);
//
//        // Create a PopupWindow object
//        PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        // Set the background drawable for the popup window
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        // Set focusable true to enable touch events outside of the popup window
//        popupWindow.setFocusable(true);
//
//        // Show the popup window at the center of the anchor view
//        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
//
//        // Call your method to update the screen name inside the custom view
//        updateScreenName(customView);
//    }
//
//
//
//
//
//    public void updateScreenName(View view) {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userEmail = user.getEmail();
//        CollectionReference collectionReference = firestore.collection(userEmail);
//
//        // Get document from collection
//        collectionReference.document("UserInfo").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    // DocumentSnapshot contains the data of the document
//                    Map<String, Object> data = document.getData();
//                    // Now you can use the 'data' map to access all fields and their values
//                    Object value = data.get("Name");
//                    String name = value != null ? String.valueOf(value) : "";
//                    // Do something with the retrieved name value, such as displaying it
//                    Log.d(TAG, "Name: " + name);
//
//                    // Call a method to handle the retrieved name value
//                    handleName(name);
//                } else {
//                    Log.d(TAG, "Error: Document doesn't exist");
//                }
//            } else {
//                Log.d(TAG, "Error: Failed to retrieve document", task.getException());
//            }
//        });
//    }
//
//    private void handleName(String name) {
//        // Here, you can use the retrieved name value as needed
//        // For example, you can update a TextView with the name
//        TextView nameTextView = findViewById(R.id.screenName);
//        nameTextView.setText(name);
//    }
//
//    private void showChangeNameDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Change Name");
//
//        // Set up the input
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS); // Capitalize words
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String newName = input.getText().toString();
//                // Handle the new name here (e.g., update user profile or display it)
//                updateUserName(newName);  // Implement this method to update the user's name
//                updateScreenName();
////                View view = null;
////                updateScreenName(view);
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }
//
//    private void updateUserName(String newName) {
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String userEmail = user.getEmail();
//        CollectionReference collectionReference = firestore.collection(userEmail);
//
//        // Create a new HashMap to hold the updated data
//        Map<String, Object> newData = new HashMap<>();
//        newData.put("Name", newName);
//
//        // Update the document with the new data
//        collectionReference.document("UserInfo")
//                .update(newData)
//                .addOnSuccessListener(aVoid -> {
//                    Log.d(TAG, "DocumentSnapshot successfully updated!");
//                    // Handle success, such as displaying a success message
//                    Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Log.d(TAG, "Error updating document", e);
//                    // Handle errors, such as displaying an error message
//                    Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show();
//                });
//    }
//
////        if (user != null) {
////            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
////                    .setDisplayName(newName)
////                    .build();
////            user.updateProfile(profileUpdates)
////                    .addOnCompleteListener(new OnCompleteListener<Void>() {
////                        @Override
////                        public void onComplete(@NonNull Task<Void> task) {
////                            if (task.isSuccessful()) {
////                                Log.d("Profile", "User name updated.");
////                            }
////                        }
////                    });
////        }
// //   }
//
//    private void sendPasswordResetEmail() {
//        // check what to call in get() method
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                // Password reset email sent successfully
//                                // You can inform the user or handle the UI accordingly
//                                Log.d("SendPasswordReset", "Password reset email sent.");
//                                // Optionally, you can show a toast or dialog to inform the user
//                                Toast.makeText(SettingsActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                // Failed to send password reset email
//                                // You can inform the user or handle the UI accordingly
//                                Log.e("SendPasswordReset", "Failed to send password reset email.", task.getException());
//                                // Optionally, you can show a toast or dialog to inform the user
//                                Toast.makeText(SettingsActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
//    }
//
//
//    public void onClickBack(View view) {
//        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
//        startActivity(intent);
//    }
//
//
//
//
//}


package com.example.healthapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//import { getAuth, sendPasswordResetEmail } from "firebase/auth";

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    Boolean darkMode;
    private Switch Switch;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean notifications;

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
        setContentView(R.layout.activity_settings);
        Switch = findViewById(R.id.lightModeSwitch);
        Switch.setChecked(sharedPreferences.getBoolean("Light", false));


        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("Light", isChecked);
                editor.apply();
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
                sendPasswordResetEmail();
            }
        });

        LinearLayout logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // After logout, clear all activities and start the LoginActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Call finish to close the current activity
            }
        });

        LinearLayout changeName = findViewById(R.id.change_name);
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNameDialog();
            }
        });
        updateScreenName();


    }



    public void updateScreenName() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        CollectionReference collectionReference = firestore.collection(userEmail);

        // Get document from collection
        collectionReference.document("UserInfo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // DocumentSnapshot contains the data of the document
                    Map<String, Object> data = document.getData();
                    // Now you can use the 'data' map to access all fields and their values
                    Object value = data.get("Name");
                    String name = value != null ? String.valueOf(value) : "";
                    // Do something with the retrieved name value, such as displaying it
                    Log.d(TAG, "Name: " + name);

                    // Call a method to handle the retrieved name value
                    handleName(name);
                } else {
                    Log.d(TAG, "Error: Document doesn't exist");
                }
            } else {
                Log.d(TAG, "Error: Failed to retrieve document", task.getException());
            }
        });
    }

    private void handleName(String name) {
        // Here, you can use the retrieved name value as needed
        // For example, you can update a TextView with the name
        TextView nameTextView = findViewById(R.id.screenName);
        nameTextView.setText(name);
    }

    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Name");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS); // Capitalize words
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)}); // Set maximum length to 12 characters
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                // Handle the new name here (e.g., update user profile or display it)
                updateUserName(newName);  // Implement this method to update the user's name
                updateScreenName();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateUserName(String newName) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        CollectionReference collectionReference = firestore.collection(userEmail);

        // Create a new HashMap to hold the updated data
        Map<String, Object> newData = new HashMap<>();
        newData.put("Name", newName);

        // Update the document with the new data
        collectionReference.document("UserInfo")
                .update(newData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                    // Handle success, such as displaying a success message
                    Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error updating document", e);
                    // Handle errors, such as displaying an error message
                    Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show();
                });
    }

//        if (user != null) {
//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(newName)
//                    .build();
//            user.updateProfile(profileUpdates)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d("Profile", "User name updated.");
//                            }
//                        }
//                    });
//        }
    //   }

    private void sendPasswordResetEmail() {
        // check what to call in get() method
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Password reset email sent successfully
                                // You can inform the user or handle the UI accordingly
                                Log.d("SendPasswordReset", "Password reset email sent.");
                                // Optionally, you can show a toast or dialog to inform the user
                                Toast.makeText(SettingsActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to send password reset email
                                // You can inform the user or handle the UI accordingly
                                Log.e("SendPasswordReset", "Failed to send password reset email.", task.getException());
                                // Optionally, you can show a toast or dialog to inform the user
                                Toast.makeText(SettingsActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    public void onClickBack(View view) {
        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(intent);
    }




}

