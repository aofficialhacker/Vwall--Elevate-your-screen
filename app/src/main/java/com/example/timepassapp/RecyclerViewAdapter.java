package com.example.timepassapp;

/*import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    private Context context;


    public RecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUrl = mData.get(position);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        Button downloadButton;
        Button wallpaperButton;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            downloadButton = itemView.findViewById(R.id.download_button);
            wallpaperButton = itemView.findViewById(R.id.set_wallpaper_button);
            itemView.setOnClickListener(this);
            downloadButton.setOnClickListener(this);
            wallpaperButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String imageUrl = mData.get(position).getImageUrl();

                switch (view.getId()) {
                    case R.id.download_button:

                        break;
                    case R.id.set_wallpaper_button:

                        break;
                    default:
                        Toast.makeText(context, "Clicked item position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }

        private void downloadImage(String imageUrl) {
            // Code to download image and save it in device storage goes here
            Toast.makeText(context, "Image downloaded", Toast.LENGTH_SHORT).show();
        }

        private void setWallpaper(String imageUrl) {
            // Code to set wallpaper goes here
            Toast.makeText(context, "Wallpaper set", Toast.LENGTH_SHORT).show();
        }
    }
}*/
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.timepassapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/*public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mExampleList;

    public RecyclerViewAdapter(Context context, List<String> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = mExampleList.get(position);

        Glide.with(mContext)
                .load(imageUrl)
                .into(holder.imageView);

        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage(imageUrl);
            }
        });

        holder.setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setWallpaper(imageUrl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public Button downloadButton;
        public Button setWallpaperButton;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            downloadButton = itemView.findViewById(R.id.download_button);
            setWallpaperButton = itemView.findViewById(R.id.set_wallpaper_button);
        }
    }

    private void downloadImage(String imageUrl) {
        Glide.with(mContext)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @NonNull Transition<? super Bitmap> transition) {
                        saveImage(resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // Do nothing
                    }
                });
    }

    private void saveImage(Bitmap bitmap) {
        String fileName = System.currentTimeMillis() + ".jpg";
        File folder = new File(Environment.getExternalStorageDirectory() + "/WallpaperApp");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            Toast.makeText(mContext, "Image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
    }}
 private void setWallpaper(String imageUrl) {
        Glide.with(mContext)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @NonNull*/
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<WallpaperItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDownloadClick(int position);
        void onSetWallpaperClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public RecyclerViewAdapter(Context context, List<WallpaperItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WallpaperItem currentItem = mExampleList.get(position);

        String imageUrl = currentItem.getImageUrl();



        Glide.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;
        public Button mDownloadButton;
        public Button mSetWallpaperButton;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mDownloadButton = itemView.findViewById(R.id.download_button);
            mSetWallpaperButton = itemView.findViewById(R.id.set_wallpaper_button);

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

            mDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDownloadClick(position);
                        }
                    }
                }
            });

            mSetWallpaperButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSetWallpaperClick(position);
                        }
                    }
                }
            });
        }
    }
}



