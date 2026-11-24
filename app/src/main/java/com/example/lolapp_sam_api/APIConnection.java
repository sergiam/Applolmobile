package com.example.lolapp_sam_api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class APIConnection {
    private static final String BASE_URL = "https://universe-meeps.leagueoflegends.com/";

    private APIConnection() {}

    private static class Holder {
        private static final Retrofit INSTANCE = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient() {
        return Holder.INSTANCE;
    }
}
