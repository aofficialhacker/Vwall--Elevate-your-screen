package com.example.timepassapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PlaylistImageAdapter extends RecyclerView.Adapter<PlaylistImageAdapter.ViewHolder> {

    private List<Uri> imageList;
    private OnRemoveClickListener onRemoveClickListener;

    public PlaylistImageAdapter(List<Uri> imageList, OnRemoveClickListener onRemoveClickListener) {
        this.imageList = imageList;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_playlist_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        // Load the image from the Uri using Glide
        Glide.with(context)
                .load(imageList.get(position))
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public ImageView removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            removeButton = itemView.findViewById(R.id.remove_button);
            removeButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == removeButton) {
                int position = getAdapterPosition();
                onRemoveClickListener.onRemoveClick(position);
            }
        }
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }
}









