package com.example.timepassapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Contact_us extends AppCompatActivity {

    private EditText mSubjectEditText;
    private EditText mMessageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Initialize views
        mSubjectEditText = findViewById(R.id.subject_edit_text);
        mMessageEditText = findViewById(R.id.message_edit_text);

        // Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        // Set click listener for send button
        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        // Get subject and message text
        String subject = mSubjectEditText.getText().toString().trim();
        String message = mMessageEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(subject)) {
            mSubjectEditText.setError("Please enter a subject");
            return;
        }

        if (TextUtils.isEmpty(message)) {
            mMessageEditText.setError("Please enter a message");
            return;
        }

        // Create intent to send email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "rajgorvishal28@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setPackage("com.google.android.gm");

        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Show error message if Gmail app is not installed
            Toast.makeText(this, "Gmail app is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate back to previous activity
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}