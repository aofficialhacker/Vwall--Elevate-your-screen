package com.example.timepassapp;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,RecyclerViewAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<WallpaperItem> wallpaperItemList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Get a reference to the spinner
        Spinner spinner = findViewById(R.id.category_spinner);

        // Set up the spinner adapter with the categories array from MainActivity
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.categoriesArray);
        spinner.setAdapter(spinnerAdapter);

        // Set the onItemSelectedListener for the spinner
        spinner.setOnItemSelectedListener(this);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.rv_category);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));



        // Initialize the RequestQueue
        mRequestQueue = Volley.newRequestQueue(this);

        wallpaperItemList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(CategoryActivity.this, wallpaperItemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(CategoryActivity.this);


    }
    //when user select category from spinner

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        // Get the selected category from the spinner
        String category = (String) adapterView.getItemAtPosition(position);

        // Call the searchImages() method with the selected category
        searchImages(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }


    private void searchImages(String category) {
        String url = "https://pixabay.com/api/?key=" + "28627810-443d6398e30814e22bbfdcd59" + "&q=" + category + "&image_type=photo";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("hits");
                        wallpaperItemList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hit = jsonArray.getJSONObject(i);

                            String imageUrl = hit.getString("webformatURL");

                            WallpaperItem wallpaperItem = new WallpaperItem(imageUrl);
                            wallpaperItemList.add(wallpaperItem);
                        }

                        recyclerViewAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(CategoryActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        mRequestQueue.add(request);
    }


    //when user click on wallpaper

    @Override
    public void onItemClick(int position) {
        WallpaperItem wallpaperItem = wallpaperItemList.get(position);
        Intent intent = new Intent(CategoryActivity.this, WallpaperDetailActivity.class);
        intent.putExtra("imageUrl", wallpaperItem.getImageUrl());
        startActivity(intent);
    }

    // Handle set wallpaper button click
    @Override
    public void onSetWallpaperClick(int position) {
        WallpaperItem wallpaperItem = wallpaperItemList.get(position);
        String imageUrl = wallpaperItem.getImageUrl();

        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(CategoryActivity.this);
                        try {
                            wallpaperManager.setBitmap(resource);
                            Toast.makeText(CategoryActivity.this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(CategoryActivity.this, "Wallpaper set failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // Handle download button click
    @Override
    public void onDownloadClick(int position) {
        WallpaperItem wallpaperItem = wallpaperItemList.get(position);

        // Get the image URL from the WallpaperItem object
        String imageUrl = wallpaperItem.getImageUrl();

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));             // url of image download is set
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //notify when download is complete
        request.setTitle("Downloading Image");      //title of downloading notification
        request.setDescription("Downloading image from Pixabay...");    //description of downloading notification

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "PixabayImage.jpg"); //where to store downloaded image

        downloadManager.enqueue(request);

        Toast.makeText(this, "Image downloading...", Toast.LENGTH_SHORT).show();
    }


}


