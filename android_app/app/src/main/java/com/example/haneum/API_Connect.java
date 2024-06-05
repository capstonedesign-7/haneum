package com.example.haneum;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Result;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class API_Connect {


    public void connect(String apiStep, File file, String idx, String lang, Result_Interface result_interface) {


        API_Interface api_interface = API_Client.getClient().create(API_Interface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
        //body
        MultipartBody.Part recordFile = MultipartBody.Part.createFormData("audio_file", file.getName(), requestFile);

        RequestBody content_idx = RequestBody.create(MediaType.parse("text/plain"), idx);

        RequestBody language_idx = RequestBody.create(MediaType.parse("text/plain"), lang);

        if (apiStep == "1") {
            api_interface.lev1_start(recordFile, content_idx,language_idx).enqueue(new Callback<Result_Class>() {

                @Override
                public void onResponse(Call<Result_Class> call, Response<Result_Class> response) {
                    if (response.code()==200) {
                        Result_Class result = response.body();
                        String a = result.getStatus();
                        Log.d("status",a);
                        result_interface.success(result);
                    }else if (response.code()==420) {
                        Log.d("error", "420 error"); // 오디오 ( 공백 또는 음성 추출 불가 ) 일 때, 토스트 메시지 등 필요할 듯

                    }
                }

                @Override
                public void onFailure(Call<Result_Class> call, Throwable t) {
                    Log.d("결과 : ", "실패");
                    result_interface.failure(t);

                    t.printStackTrace();
                }
            });
        } else if (apiStep == "2") {
            api_interface.lev2_start(recordFile, content_idx,language_idx).enqueue(new Callback<Result_Class>() {

                @Override
                public void onResponse(Call<Result_Class> call, Response<Result_Class> response) {
                    if (response.code()==200) {
                        Result_Class result = response.body();
                        String a = result.getStatus();
                        Log.d("status",a);
                        Log.d("aaa",result.getStt_result());
                        result_interface.success(result);
                    }else if (response.code()==420) {
                            Log.d("error", "420 error"); // 오디오 ( 공백 또는 음성 추출 불가 ) 일 때, 토스트 메시지 등 필요할 듯
                    }
                }

                @Override
                public void onFailure(Call<Result_Class> call, Throwable t) {
                    Log.d("결과 : ", "실패");
                    result_interface.failure(t);

                    t.printStackTrace();
                }
            });
        }


    }
}
