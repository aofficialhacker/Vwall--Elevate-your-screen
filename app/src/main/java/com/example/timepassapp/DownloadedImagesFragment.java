package com.example.timepassapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadedImagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private DownloadedImagesAdapter adapter;
    private List<String> downloadedImagesList = new ArrayList<>();

    public DownloadedImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout searchLayout = getActivity().findViewById(R.id.ll);
        searchLayout.setVisibility(View.GONE);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloaded_images, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rv_downloaded_images);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize adapter
        adapter = new DownloadedImagesAdapter(getContext(), downloadedImagesList);
        recyclerView.setAdapter(adapter);

        // Load downloaded images from storage
        loadDownloadedImages();

        return view;
    }

    private void loadDownloadedImages() {
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = downloadsDir.listFiles();


        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".jpg")) {
                    downloadedImagesList.add(file.getAbsolutePath());
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

}
