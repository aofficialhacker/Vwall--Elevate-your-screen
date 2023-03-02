package com.example.timepassapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.timepassapp.R;
import com.example.timepassapp.RecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;



import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<WallpaperItem> wallpaperItemList;
    private RequestQueue mRequestQueue;
    private String imageUrl;
    private Timer timer;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private int interval;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // Set up the navigation drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Toast.makeText(MainActivity.this,"vishal",Toast.LENGTH_SHORT).show();

                if (id == R.id.nav_settings) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container,new SettingsFragment())
                            .commit();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Initialize the RequestQueue
        mRequestQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        wallpaperItemList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, wallpaperItemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(MainActivity.this);
        imageUrl = getIntent().getStringExtra("imageUrl");

        // Get a reference to the EditText
        EditText editText = findViewById(R.id.searchEditText);

// Set an OnEditorActionListener to listen for the search action
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Get the search query from the EditText
                    String query = editText.getText().toString().trim();

                    // Call the searchImages method with the search query
                    searchImages(query);

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
        // Find the search button
        Button searchButton = findViewById(R.id.searchButton);

// Set an OnClickListener on the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editText.getText().toString().trim();
                searchImages(query);
            }
        });


    }


    private void searchImages(String query) {
        String url = "https://pixabay.com/api/?key=" + "28627810-443d6398e30814e22bbfdcd59" + "&q=" + query + "&image_type=photo";


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
            Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        mRequestQueue.add(request);
    }


    @Override
    public void onItemClick(int position) {
        WallpaperItem wallpaperItem = wallpaperItemList.get(position);
        Intent intent = new Intent(MainActivity.this, WallpaperDetailActivity.class);
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
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
                        try {
                            wallpaperManager.setBitmap(resource);
                            Toast.makeText(MainActivity.this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Wallpaper set failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Handle download button click
    @Override
    public void onDownloadClick(int position) {
        WallpaperItem wallpaperItem = wallpaperItemList.get(position);
        String imageUrl = wallpaperItem.getImageUrl();
        DownloadTask downloadTask = new DownloadTask(MainActivity.this, imageUrl);
        downloadTask.execute();
    }

    public class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context mContext;
        private ProgressDialog mProgressDialog;
        private String mFileName;

        public DownloadTask(Context context, String fileName) {
            mContext = context;
            mFileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress dialog
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setTitle("Downloading");
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.connect();
                int fileLength = connection.getContentLength();
                File outputFile = new File(mContext.getExternalFilesDir(null), mFileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int len;
                long total = 0;
                while ((len = is.read(buffer)) != -1) {
                    total += len;
                    publishProgress((int) (total * 100 / fileLength));
                    fos.write(buffer, 0, len);
                }
                fos.close();
                is.close();
                return "Downloaded successfully";
            } catch (IOException e) {
                return "Download failed: " + e.getMessage();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // dismiss progress dialog and show Toast message
            mProgressDialog.dismiss();
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        }
    }

    //change wallpaper automatically

    public void changeWallpaperAutomatically(int intervalInSeconds) {
        List<File> wallpaperFiles = getDownloadedWallpaperFiles();
        if (!wallpaperFiles.isEmpty()) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                int index = 0;
                @Override
                public void run() {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
                    try {
                        Uri uri = Uri.fromFile(wallpaperFiles.get(index));
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        wallpaperManager.setBitmap(bitmap);
                        index = (index + 1) % wallpaperFiles.size();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, intervalInSeconds * 1000);
        } else {
            Toast.makeText(this, "No wallpaper found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopChangingWallpaperAutomatically() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Toast.makeText(MainActivity.this, "Automatic wallpaper change stopped", Toast.LENGTH_SHORT).show();
        }
    }





    // Method to get downloaded wallpaper files
    private List<File> getDownloadedWallpaperFiles() {
        List<File> wallpaperFiles = new ArrayList<>();
        File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = downloadDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
            }
        });
        if (files != null) {
            for (File file : files) {
                wallpaperFiles.add(file);
            }
        }
        return wallpaperFiles;
    }
    private void openSettingsFragment() {
        // Create the settings fragment and show it
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new SettingsFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    // Handle the back button when the navigation drawer is open
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // Open the navigation drawer when the menu button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }






}




