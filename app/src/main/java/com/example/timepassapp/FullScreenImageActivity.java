package com.example.timepassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        // Get the image URL from the intent
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the image using an image loading library like Glide or Picasso
        ImageView imageView = findViewById(R.id.full_screen_image_view);
        Glide.with(this).load(imageUrl).dontTransform().into(imageView); //'this' is a context here that gives access to application resources
    }
}
