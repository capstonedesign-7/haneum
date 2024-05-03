package com.example.haneum;

import java.util.List;
import com.example.haneum.Pronounce_Class;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/* API Interface */
public interface API_Interface {
    /* Test code */
    /*
    @GET("/posts") // baseURL 뒷 부분 주소
    Call<List<Pronounce_Class>> getData(@Query("userId") String id); */

    /*
    @GET("/texts/0")
    Call<String> getText();
    */
    /*
    @Multipart
    @POST("/assessment")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part audio_file,
            @Part ("text_idx") RequestBody text_idx
    );
*/
    /*
    @POST("/testqu")
    Call<ResponseBody> uuu(@Query("item") String item);
    */

    //@Headers("Content-Type: application/json")
    /*
    @FormUrlEncoded
    @POST("/teststr")
    Call<ResponseBody> uppload(
            @Field("item") String item
    );*/

    @Multipart
    @POST("/assessment")Call<ResponseBody> update(
            @Part MultipartBody.Part audio_file,
            @Part ("text_idx") RequestBody text_idx
    );

}
