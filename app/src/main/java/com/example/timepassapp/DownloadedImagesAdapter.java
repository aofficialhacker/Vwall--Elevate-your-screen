package com.example.timepassapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class DownloadedImagesAdapter extends RecyclerView.Adapter<DownloadedImagesAdapter.ViewHolder> {

    private Context context;
    private List<String> downloadedImagesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public DownloadedImagesAdapter(Context context, List<String> downloadedImagesList) {
        this.context = context;
        this.downloadedImagesList = downloadedImagesList;
    }

    @NonNull
    @Override
    //inflate layout of item
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_downloaded_image, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    //bind data of an item to item view
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = downloadedImagesList.get(position);
        Glide.with(context)
                .load(new File(imagePath))
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return downloadedImagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_downloaded_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}


