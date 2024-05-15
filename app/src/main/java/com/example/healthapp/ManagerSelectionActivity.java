//Reused Code for Notis To get emails that manager can click on to see stats

package com.example.healthapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerSelectionActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    String[] emailArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_selection);
        onClickUpdate();
    }


    //reused notis function to get notis but instead it gets all the emails of the users which are stored in JustEmails in FireBase
    public void onClickUpdate() {
        firestore = FirebaseFirestore.getInstance();
        ArrayList<String> allNotis = new ArrayList<>();

        CollectionReference collectionReference = firestore.collection("JustEmails");

        // Get document from collection
        collectionReference.document("Emails").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    for (String key : data.keySet()) {
                        Object value = data.get(key);


                        String valueString = String.valueOf(value);
                        allNotis.add(valueString);
                        Log.d(TAG, "Field: " + key + ", Value: " + value);
                    }
                    updateNotificationTextView(allNotis);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });





    }




    //reused the notis function for the view for selecting emails for the manager
    private void updateNotificationTextView(ArrayList<String> allNotis) {
        // Find the parent LinearLayout where you want to add the TextViews
        LinearLayout parentLayout = findViewById(R.id.notificationsLayout); // Replace R.id.parentLayout with the ID of your parent LinearLayout

        // Iterate through each emails strings
        for (String notification : allNotis) {
            // Create a new TextView
            TextView textView = new TextView(this);

            // Set the text of the TextView
            textView.setText(notification);
            textView.setTextSize(18);
            textView.setPadding(50, 50, 50, 50);
            textView.setTextColor(getResources().getColor(android.R.color.white));

            // Set the background drawable
            textView.setBackgroundResource(R.drawable.rounded_corners_background);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT // Use WRAP_CONTENT for TextView height
            );
            params.setMargins(50, 40, 50,  40); // Adjust margins as needed
            textView.setLayoutParams(params);

            textView.setOnClickListener(v -> {
                //Passes the Email of selected user to the Stats Screen

                Intent intent = new Intent(ManagerSelectionActivity.this, ParticipantInformationActivity.class);
                intent.putExtra("Email", textView.getText());
                startActivity(intent);

            });

            // Add the TextView to the parent layout
            parentLayout.addView(textView);
        }



    }



    public void onClickBack(View view) {
        Intent intent = new Intent(ManagerSelectionActivity.this, ManagerHomeActivity.class);
        startActivity(intent);
    }

    public void onClickGroup(View view) {
        Intent intent = new Intent(ManagerSelectionActivity.this, ParticipantInformationActivity.class);
        intent.putExtra("Email", "GroupStats");
        startActivity(intent);
    }



}