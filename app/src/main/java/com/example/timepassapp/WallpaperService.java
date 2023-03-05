package com.example.timepassapp;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WallpaperService extends Service {

    private static final String TAG = "WallpaperService";
    private List<Uri> imageList = new ArrayList<>();
    private int currentImageIndex = 0;
    private boolean isRunning = false;
    private Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (!isRunning) {
            isRunning = true;
            loadImages();
            startChangingWallpapers();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        isRunning = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void loadImages() {
        // Load the saved images from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        Set<String> imageSet = sharedPreferences.getStringSet("image_list", null);
        if (imageSet != null) {
            for (String uriString : imageSet) {
                Uri uri = Uri.parse(uriString);
                imageList.add(uri);
            }
        }
    }

    private void startChangingWallpapers() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set the wallpaper logic
                if (!imageList.isEmpty()) {
                    Uri wallpaperUri = imageList.get(currentImageIndex);
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                    try {
                        wallpaperManager.setStream(getContentResolver().openInputStream(wallpaperUri));
                        currentImageIndex++;
                        if (currentImageIndex >= imageList.size()) {
                            currentImageIndex = 0;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (isRunning) {
                    handler.postDelayed(this, 5000); // Set the delay to 5 seconds (5000 milliseconds)
                }
            }
        }, 5000); // Set the initial delay to 5 seconds (5000 milliseconds)
    }

}

