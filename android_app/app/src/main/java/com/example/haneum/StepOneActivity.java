package com.example.haneum;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class StepOneActivity extends AppCompatActivity  implements View.OnClickListener{

    Button btnstart, btnstop;
    MediaRecorder mediaRecorder;
    String filepath, filepath2;
    String testString;
    LinearLayout listView;

    JSONObject jObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepone);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));

        btnstart = findViewById(R.id.button5);
        btnstop = findViewById(R.id.button6);

        btnstart.setOnClickListener(this);
        btnstop.setOnClickListener(this);


        filepath2 = getCacheDir().getAbsolutePath();
        filepath2 += "/audiorecordtest2.mp3";
        Log.d("filepath",filepath2);



    }
    public void onClick(View v){
        if(v == btnstart){
            Log.d("성공", "hi2");
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile(filepath2);

            /* test */
            /*
            listView = findViewById(R.id.linearLayout3);

            for (int i = 0; i< 2; i++){
                String t = Integer.toString(i);
                TextView textViewT = new TextView(getApplicationContext());
                textViewT.setText("단어" + t + " : ");
                TextView textViewT1 = new TextView(getApplicationContext());
                textViewT1.setText("점수" + t + " : ");
                TextView textViewT2 = new TextView(getApplicationContext());
                textViewT2.setText("타입" + t + " : ");
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                param.leftMargin = 20;

                textViewT.setLayoutParams(param);
                listView.addView(textViewT);
                textViewT1.setLayoutParams(param);
                listView.addView(textViewT1);
                textViewT2.setLayoutParams(param);
                listView.addView(textViewT2);


            }
            */


            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        else if(v == btnstop){
            Log.d("성공", "hi3");
            if(mediaRecorder == null){
                return;
            }
            else {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder=null;

                /* */
                //String filename = "test_audio.m4a";

                String filename = "test-2.m4a";
                filepath = this.getCacheDir().getAbsolutePath();
                filepath += "/" + filename;

                File file = new File(filepath); // audio file path

                API_Interface api_interface2 = API_Client.getClient().create(API_Interface.class);

                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("audio_file", file.getName(), requestFile);

                RequestBody requestFile2 = RequestBody.create(MediaType.parse("text/plain"), "2");

                Log.d("시작","start");

                api_interface2.update(body, requestFile2).enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                        if(response.isSuccessful()){
                            String data = null;


                            try {
                                data = response.body().string();
                                Log.d("data", data);

                                Log.d("결과 : ", "성공");
                                Log.d("값", data);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                            try {
                                jObject = new JSONObject(data);
                                String jStatus = jObject.getString("status");
                                Log.d("jStatus", jStatus.toString());

                                JSONObject jTotalScore = jObject.getJSONObject("total_score");
                                String jPronScore = jTotalScore.getString("pron_score");
                                String jAccurScore = jTotalScore.getString("accuracy_score");
                                String jCompleScore = jTotalScore.getString("completeness_score");
                                String jFluScore = jTotalScore.getString("fluency_score");

                                TextView ttt1 = findViewById(R.id.textView7);
                                ttt1.setText(jPronScore);
                                TextView ttt2 = findViewById(R.id.textView10);
                                ttt2.setText(jAccurScore);
                                TextView ttt3 = findViewById(R.id.textView12);
                                ttt3.setText(jCompleScore);
                                TextView ttt4 = findViewById(R.id.textView14);
                                ttt4.setText(jFluScore);

                                JSONObject jWordsScore = jObject.getJSONObject("words_score");
                                int leng = jWordsScore.length();
                                listView = findViewById(R.id.linearLayout3);

                                for (int i = 0; i < leng; i++){
                                    String t = Integer.toString(i);
                                    String aaa = jWordsScore.getString(t);
                                    String[] strArr = aaa.split("\\[|,|\\]");
                                    Log.d("aaa", strArr[0]); // 빈공간 출력
                                    Log.d("bbb", strArr[1]);
                                    Log.d("ccc", strArr[2]);
                                    Log.d("ddd",strArr[3]);
                                    // 동적 레이아웃 추가

                                    TextView textViewT = new TextView(getApplicationContext());
                                    textViewT.setText("단어" + t + " : " + strArr[1]);
                                    TextView textViewT1 = new TextView(getApplicationContext());
                                    textViewT1.setText("점수" + t + " : " + strArr[2]);
                                    TextView textViewT2 = new TextView(getApplicationContext());
                                    textViewT2.setText("타입" + t + " : " + strArr[3]);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    param.leftMargin = 20;

                                    textViewT.setLayoutParams(param);
                                    listView.addView(textViewT);
                                    textViewT1.setLayoutParams(param);
                                    listView.addView(textViewT1);
                                    textViewT2.setLayoutParams(param);
                                    listView.addView(textViewT2);
                                }


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.d("결과 : ", "실패");

                        t.printStackTrace();
                    }
                });

                /*
                testString =
                        "{"
                        + "\"status\":\"ok\","
                        + "\"total_score\":"
                        + "{"
                        + "\"pron_score\":98.56, \"accuracy_score\":96.4, \"completeness_score\":100.0, \"fluency_score\":100.0"
                        + "},"
                        + "\"words_score\":"
                        + "{"
                        + "\"김서방네\" : [100.0,\"None\"], \"지붕위에\":[100.0,\"None\"], \"콩깍지가\":[100.0,\"None\"], \"깐\":[100.0,\"None\"], \"콩깍지냐\":[82.0,\"None\"]"
                        + "}}";

                Log.d("끝","end");
                Log.d("testText", testString);

                try {
                    jObject = new JSONObject(testString);

                    String jStatus = jObject.getString("status");
                    Log.d("jStatus", jStatus.toString());

                    JSONObject jTotalScore = jObject.getJSONObject("total_score");
                    String jPronScore = jTotalScore.getString("pron_score");
                    String jAccurScore = jTotalScore.getString("accuracy_score");
                    String jCompleScore = jTotalScore.getString("completeness_score");
                    String jFluScore = jTotalScore.getString("fluency_score");


                    JSONObject jWordsScore = jObject.getJSONObject("words_score");
                    String aaa = jWordsScore.getString("김서방네");
                    String[] strArr = aaa.split("\\[|,|\\]");

                    Log.d("aaa", strArr[0]);
                    Log.d("bbb", strArr[1]);
                    Log.d("ccc", strArr[2]);

                    TextView ttt1 = findViewById(R.id.textView7);
                    ttt1.setText(jPronScore);

                    TextView ttt2 = findViewById(R.id.textView10);
                    ttt2.setText(jAccurScore);
                    TextView ttt3 = findViewById(R.id.textView12);
                    ttt3.setText(jCompleScore);
                    TextView ttt4 = findViewById(R.id.textView14);
                    ttt4.setText(jFluScore);




                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }*/

            }
        }

    }
}
