package com.example.haneum;

import java.util.List;
import com.example.haneum.Pronounce_Class;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/* API Interface */
public interface API_Interface {
    /* Test code */
    @GET("/posts") // baseURL 뒷 부분 주소
    Call<List<Pronounce_Class>> getData(@Query("userId") String id);

    /*
    @Multipart
    @POST("")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file
    );
    */
}
