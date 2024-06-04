package com.example.haneum;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;



/* API Interface */
public interface API_Interface {

    @Multipart
    @POST("/lev1_assessment")Call<Result_Class> lev1_start(
            @Part MultipartBody.Part audio_file,
            @Part ("content_idx") RequestBody content_idx,
            @Part ("language_idx") RequestBody language_idx
    );

    @Multipart
    @POST("/lev2_assessment")Call<Result_Class> lev2_start(
            @Part MultipartBody.Part audio_file,
            @Part ("content_idx") RequestBody content_idx,
            @Part ("language_idx") RequestBody language_idx
    );


}
