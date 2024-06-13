package com.example.haneum;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/* API Client */
public class API_Client {

    private static Retrofit retrofit = null;

    static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.8:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
