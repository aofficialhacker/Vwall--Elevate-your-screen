package com.example.timepassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class about_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        // Set text content
        TextView aboutUsTextView = findViewById(R.id.about_us_text_view);
        aboutUsTextView.setText(R.string.about_us_content);

    }
}