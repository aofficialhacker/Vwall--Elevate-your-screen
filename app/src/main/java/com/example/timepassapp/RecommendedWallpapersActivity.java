package com.example.timepassapp;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

public class RecommendedWallpapersActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<WallpaperItem> wallpaperItemList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_wallpapers);

        recyclerView = findViewById(R.id.recyclerViewRecommendedWallpapers);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        wallpaperItemList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(RecommendedWallpapersActivity.this, wallpaperItemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(RecommendedWallpapersActivity.this);

        mRequestQueue = Volley.newRequestQueue(this);
        loadRecommendedWallpapers();
    }

    private void loadRecommendedWallpapers() {
        String url = "https://pixabay.com/api/?key=" + "28627810-443d6398e30814e22bbfdcd59" + "&q=" + "mobile wallpapers" + "&image_type=photo";

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
            Toast.makeText(RecommendedWallpapersActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        WallpaperItem wallpaperItem = wallpaperItemList.get(position);
        Intent intent = new Intent(RecommendedWallpapersActivity.this, WallpaperDetailActivity.class);
        intent.putExtra("imageUrl", wallpaperItem.getImageUrl());
        startActivity(intent);
    }

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
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(RecommendedWallpapersActivity.this);
                        try {
                            wallpaperManager.setBitmap(resource);
                            Toast.makeText(RecommendedWallpapersActivity.this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(RecommendedWallpapersActivity.this, "Wallpaper set failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onDownloadClick(int position) {
        WallpaperItem wallpaperItem = wallpaperItemList.get(position);

        String imageUrl = wallpaperItem.getImageUrl();

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