package com.example.timepassapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Imagenew> mImageList;

    public ImageAdapter(Context context, List<Imagenew> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image2, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Imagenew currentImage = mImageList.get(position);
        holder.textViewName.setText(currentImage.getImageName());
        Picasso.get().load(currentImage.getImageUrl()).fit().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.image_name);
            imageView = itemView.findViewById(R.id.new_image_view);
        }
    }
}





