package com.example.timepassapp;

public class WallpaperItem {
    private String mImageUrl;
    private String mCreatorName;

    public WallpaperItem(String imageUrl) {
        mImageUrl = imageUrl;

    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreatorName() {
        return mCreatorName;
    }
}

