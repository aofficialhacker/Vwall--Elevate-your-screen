package com.example.timepassapp;

import android.content.Context;
import android.content.Intent;
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

//this is for uploadImageActivity

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Imagenew> mImageList;

    public ImageAdapter(Context context, List<Imagenew> imageList) {
        mContext = context;
        mImageList = imageList;
    }

    //creating new viewholder object of an item to display that item

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image2, parent, false);
        return new ImageViewHolder(view);
    }

    //bind data to view holder

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Imagenew currentImage = mImageList.get(position);

        holder.mTextView.setText(currentImage.getImageName());
        Picasso.get().load(currentImage.getImageUrl()).into(holder.mImageView);

        //when user clicks on image item

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WallpaperDetailActivity.class);
                intent.putExtra("imageUrl", currentImage.getImageUrl());
                intent.putExtra("imageName", currentImage.getImageName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    //hold references to views,here textview or imageview are held as reference(improves performance of recycler view)

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ImageView mImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.image_name);
            mImageView = itemView.findViewById(R.id.new_image_view);
        }
    }
}






