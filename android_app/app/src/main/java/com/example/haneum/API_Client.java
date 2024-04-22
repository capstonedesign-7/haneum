package com.example.haneum;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* API Client */
public class API_Client {

    private static Retrofit retrofit = null;

    static Retrofit getClient() {
        /*
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.8:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }
}
