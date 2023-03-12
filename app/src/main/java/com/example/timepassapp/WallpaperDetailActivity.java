package com.example.timepassapp;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.timepassapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WallpaperDetailActivity extends AppCompatActivity {

    private ImageView wallpaperImageView;
    private ImageButton setWallpaperButton, downloadButton,shareButton;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        wallpaperImageView = findViewById(R.id.wallpaper_image_detail);
        setWallpaperButton = findViewById(R.id.set_wallpaper_button_detail);
        downloadButton = findViewById(R.id.download_button_detail);
        shareButton = findViewById(R.id.share_button_detail);

        // Get the image url from the intent
        imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the image into the image view using Glide
        Glide.with(this)
                .load(imageUrl)
                .dontTransform()
                .placeholder(R.drawable.placeholder)
                .into(wallpaperImageView);

        // Set a click listener for the Share button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });






        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage();
            }
        });
    }

    private void setWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

        try {
            // Get the bitmap from the image view
            Bitmap bitmap = ((BitmapDrawable) wallpaperImageView.getDrawable()).getBitmap();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Set Wallpaper");
            builder.setItems(new CharSequence[]{"Home Screen", "Lock Screen", "Both"},
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    try {
                                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                                        Toast.makeText(WallpaperDetailActivity.this, "Wallpaper set to Home Screen successfully", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        Toast.makeText(WallpaperDetailActivity.this, "Failed to set wallpaper to Home Screen", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1:
                                    try {
                                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                                        Toast.makeText(WallpaperDetailActivity.this, "Wallpaper set to Lock Screen successfully", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        Toast.makeText(WallpaperDetailActivity.this, "Failed to set wallpaper to Lock Screen", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 2:
                                    try {
                                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                                        Toast.makeText(WallpaperDetailActivity.this, "Wallpaper set to both Home Screen and Lock Screen successfully", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        Toast.makeText(WallpaperDetailActivity.this, "Failed to set wallpaper to both Home Screen and Lock Screen", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }
                        }
                    });
            builder.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
        }
    }


    private void downloadImage() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("Downloading Image");
        request.setDescription("Downloading image from Pixabay...");

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "PixabayImage.jpg");

        downloadManager.enqueue(request);

        Toast.makeText(this, "Image downloading...", Toast.LENGTH_SHORT).show();
    }

    private void shareImage() {
        // Create an Intent to share the image
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        // Get the bitmap of the image from the ImageView
        BitmapDrawable drawable = (BitmapDrawable) wallpaperImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Save the bitmap to a file
        String fileName = "wallpaper.png";
        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the Uri of the saved file
        File file = new File(getFilesDir(), fileName);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);


        // Add the Uri to the share Intent
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        // Add the text to the intent
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This wallpaper is downloaded with VWall App");

        // Start the share Intent
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }
}


