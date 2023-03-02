package com.example.timepassapp;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.timepassapp.R;

import java.io.IOException;

/*public class WallpaperDetailActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Button mSetWallpaperButton;
    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        mImageView = findViewById(R.id.wallpaper_image_detail);
        mSetWallpaperButton = findViewById(R.id.set_wallpaper_button_detail);

        mImageUrl = getIntent().getStringExtra("image_url");

        Glide.with(this)
                .load(mImageUrl)
                .into(mImageView);

        mSetWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper();
            }
        });
    }

    private void setWallpaper() {
        mSetWallpaperButton.setVisibility(View.GONE);


        Glide.with(this)
                .asBitmap()
                .load(mImageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        new SetWallpaperAsyncTask().execute(resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                    }
                });
    }

    private class SetWallpaperAsyncTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bitmap... bitmaps) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmaps[0]);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mProgressBar.setVisibility(View.GONE);
            if (success) {
                Toast.makeText(getApplicationContext(), "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
                mSetWallpaperButton.setVisibility(View.VISIBLE);
            }
        }
    }
}*/
public class WallpaperDetailActivity extends AppCompatActivity {

    private ImageView wallpaperImageView;
    private Button setWallpaperButton, downloadButton;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_detail);

        wallpaperImageView = findViewById(R.id.wallpaper_image_detail);
        setWallpaperButton = findViewById(R.id.set_wallpaper_button_detail);
        downloadButton = findViewById(R.id.download_button_detail);

        // Get the image url from the intent
        imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the image into the image view using Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(wallpaperImageView);

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
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
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
}


