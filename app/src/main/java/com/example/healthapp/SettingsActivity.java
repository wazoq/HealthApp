package com.example.healthapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // Assuming your layout file is named activity_settings.xml

        // Assuming forwardArrowImg is your ImageButton
        ImageButton forwardArrowImg = findViewById(R.id.forwardArrowImg);

        // Load the original image resource
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable originalDrawable = getResources().getDrawable(R.drawable.forward_arrow);

        // Calculate the desired width and height for the image
        int desiredWidth = 50; // Set your desired width
        int desiredHeight = 50; // Set your desired height

        // Scale down the original image to the desired dimensions while maintaining aspect ratio
        Drawable scaledDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) originalDrawable).getBitmap(), desiredWidth, desiredHeight, true));

        // Set the scaled drawable as the image source for the ImageButton
        forwardArrowImg.setImageDrawable(scaledDrawable);
    }
}

