package com.example.timepassapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;




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
    public static final String[] categoriesArray =
            {"Nature", "Abstract", "Food",
            "Travel", "Textures",
            "Architecture", "Music", "Sports",
            "Space", "Art", "Technology"};

    private SwipeRefreshLayout swipeRefreshLayout;
    private CollectionReference wallpapersCollection;
    private int searchResultsCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);





        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Set wallpapersCollection reference
        wallpapersCollection = db.collection("uploads");

        // Initialize the RequestQueue
        mRequestQueue = Volley.newRequestQueue(this);

        // Display random wallpapers
        String category = categoriesArray[new Random().nextInt(categoriesArray.length)];
        searchImages(category);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load new wallpapers here
                // Display random wallpapers
                String category = categoriesArray[new Random().nextInt(categoriesArray.length)];
                searchImages(category);
                // Load wallpapers here
                stopRefreshing();

            }
        });


        // Set up the navigation drawer
        drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_contact_us) {
                    Intent intent = new Intent(MainActivity.this, Contact_us.class);
                    startActivity(intent);
                }



                if (id == R.id.about_us) {
                    Intent intent = new Intent(MainActivity.this, about_us.class);
                    startActivity(intent);
                }


                if (id == R.id.nav_uploads) {
                    Intent intent = new Intent(MainActivity.this, UploadImageActivity.class);
                    startActivity(intent);
                }

                if (id == R.id.nav_playlist) {
                    // Replace with DownloadedImagesFragment

                    Intent intent = new Intent(MainActivity.this, PlaylistActivity.class);
                    startActivity(intent);

                }

                if (id == R.id.nav_downloads) {
                    // Replace with DownloadedImagesFragment

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new DownloadedImagesFragment())
                            .commit();

                }


                if (id == R.id.nav_home) {
                    // Navigate to MainActivity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                if (id == R.id.nav_settings) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container,new SettingsFragment())
                            .commit();
                }
                if(id==R.id.nav_gallery)
                {
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent);

                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });



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
        ImageButton searchButton = findViewById(R.id.searchButton);

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
        String pixabayUrl = "https://pixabay.com/api/?key=" + "28627810-443d6398e30814e22bbfdcd59" + "&q=" + query + "&image_type=photo";

        // Search Pixabay API
        JsonObjectRequest pixabayRequest = new JsonObjectRequest(Request.Method.GET, pixabayUrl, null,
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

                        // Notify RecyclerView adapter after both API and database search
                        searchResultsCount++;
                        if (searchResultsCount == 2) {
                            recyclerViewAdapter.notifyDataSetChanged();
                            searchResultsCount = 0;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        mRequestQueue.add(pixabayRequest);

        // Search Firestore database
        Query firestoreQuery = wallpapersCollection.whereEqualTo("tags." + query.toLowerCase(), true);
        firestoreQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String imageUrl = document.getString("url");
                    WallpaperItem wallpaperItem = new WallpaperItem(imageUrl);
                    wallpaperItemList.add(wallpaperItem);
                }

                // Notify RecyclerView adapter after both API and database search
                searchResultsCount++;
                if (searchResultsCount == 2) {
                    recyclerViewAdapter.notifyDataSetChanged();
                    searchResultsCount = 0;
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Set Wallpaper");
        builder.setItems(new CharSequence[]{"Home Screen", "Lock Screen"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        setWallpaper(imageUrl, WallpaperManager.FLAG_SYSTEM);
                        break;
                    case 1:
                        setWallpaper(imageUrl, WallpaperManager.FLAG_LOCK);
                        break;
                }
            }
        });
        builder.show();
    }

    private void setWallpaper(String imageUrl, int flag) {
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .dontTransform()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
                        try {
                            wallpaperManager.setBitmap(resource, null, true, flag);
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

        // Get the image URL from the WallpaperItem object
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
        // Check if the drawer is open and close it if it is
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // Otherwise, go back to the main activity
            super.onBackPressed();
        }
    }

    private void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }










}




