package com.example.haneum;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Path;


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

    @Multipart
    @POST("/chat/{roleplay}")Call<ResponseBody> lev3_play(
            @Path("roleplay") String roleplay,
            @Part MultipartBody.Part audio_file,
            @Part ("history_msg") RequestBody history_msg
    );
   @Multipart
   @POST("/{roleplay}/check_goals") Call<ResponseBody> checkGoals(
            @Path("roleplay") String roleplay,
            @Part("history_msg") RequestBody history_msg
    );


    @Multipart
    @POST("/text_to_speech")Call<ResponseBody> textToSpeech(
            @Part("response_txt") RequestBody response_txt
    );

}



