package com.example.timepassapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PixabayApi {
    private static final String API_URL = "https://pixabay.com/api/";
    private static final String API_KEY = "28627810-443d6398e30814e22bbfdcd59";
    private static final String IMAGE_TYPE = "photo";

    public interface Callback {
        void onResponse(List<Image> images);

        void onFailure(Throwable t);
    }

    public void searchImages(String query, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addQueryParameter("key", API_KEY);
        urlBuilder.addQueryParameter("q", query);
        urlBuilder.addQueryParameter("image_type", IMAGE_TYPE);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String json = response.body().string();
                JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
                JsonArray jsonArray = jsonObject.getAsJsonArray("hits");

                List<Image> images = new ArrayList<>();
                for (JsonElement element : jsonArray) {
                    JsonObject hit = element.getAsJsonObject();
                    Image image = new Image();
                    image.url = hit.get("webformatURL").getAsString();
                    image.width = hit.get("webformatWidth").getAsInt();
                    image.height = hit.get("webformatHeight").getAsInt();
                    images.add(image);
                }

                callback.onResponse(images);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }
        });
    }
}

