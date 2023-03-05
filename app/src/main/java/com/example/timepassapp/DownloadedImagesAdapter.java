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

import java.util.List;

public class DownloadedImagesAdapter extends RecyclerView.Adapter<DownloadedImagesAdapter.ViewHolder> {

    private Context context;
    private List<String> downloadedImagesList;

    public DownloadedImagesAdapter(Context context, List<String> downloadedImagesList) {
        this.context = context;
        this.downloadedImagesList = downloadedImagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_downloaded_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = downloadedImagesList.get(position);

        Glide.with(context)
                .load(imagePath)
                .dontTransform()
                .into(holder.ivDownloadedImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullScreenImageActivity.class);
                intent.putExtra("imagePath", imagePath);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadedImagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDownloadedImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivDownloadedImage = itemView.findViewById(R.id.image_view_downloaded_image);
        }
    }
}

