package com.example.timepassapp;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaylistActivity extends AppCompatActivity implements PlaylistImageAdapter.OnRemoveClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    private List<Uri> imageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlaylistImageAdapter adapter;
    private SharedPreferences sharedPreferences;
    private Button savePlaylistButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Handler handler = new Handler();
        sharedPreferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

        recyclerView = findViewById(R.id.wallpaper_list_recyclerview);
        adapter = new PlaylistImageAdapter(imageList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));



        Button addImageButton = findViewById(R.id.btn_add_image);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        savePlaylistButton = findViewById(R.id.btn_save_playlist);
        savePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the playlist logic

                // Get the Editor object of the SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Convert the List of Uri objects to a Set of String objects
                Set<String> imageSet = new HashSet<>();
                for (Uri uri : imageList) {
                    imageSet.add(uri.toString());
                }

                // Save the Set of String objects to SharedPreferences
                editor.putStringSet("image_list", imageSet);
                editor.apply();

                // Start the WallpaperService
                startService(new Intent(PlaylistActivity.this, WallpaperService.class));

                // Set the wallpaper logic
                handler.removeCallbacksAndMessages(null); // Remove any previous callbacks
                handler.postDelayed(new Runnable() {
                    int index = 0;

                    @Override
                    public void run() {
                        Uri wallpaperUri = imageList.get(index);
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            wallpaperManager.setStream(getContentResolver().openInputStream(wallpaperUri));
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(PlaylistActivity.this, "Failed to set wallpaper", Toast.LENGTH_SHORT).show();
                        }
                        index++;
                        if (index >= imageList.size()) {
                            index = 0;
                        }
                        handler.postDelayed(this, 5000);
                    }
                }, 5000); // Set the initial delay to 5 seconds (5000 milliseconds)
            }
        });
        loadSavedImages(); // Load the saved images from SharedPreferences
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageList.add(imageUri);
            adapter.notifyItemInserted(imageList.size() - 1);
        }
    }

    @Override
    public void onRemoveClick(int position) {
        imageList.remove(position);
        adapter.notifyItemRemoved(position);
    }
    private void loadSavedImages() {
        // Load the saved images from SharedPreferences
        Set<String> imageSet = sharedPreferences.getStringSet("image_list", null);
        if (imageSet != null) {
            for (String uriString : imageSet) {
                Uri uri = Uri.parse(uriString);
                imageList.add(uri);
                adapter.notifyItemInserted(imageList.size() - 1);
            }
        }
        savePlaylistButton.setVisibility(View.VISIBLE);
    }


}



























