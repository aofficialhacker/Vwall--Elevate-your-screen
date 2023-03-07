package com.example.timepassapp;

public class Imagenew {

    private String id;
    private String imageName;
    private String imageUrl;

    public Imagenew() {
        // Default constructor required for calls to DataSnapshot.getValue(Image.class)
    }

    public Imagenew(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}




