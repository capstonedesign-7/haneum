package com.example.haneum;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.view.LayoutInflater;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaRecorder;
import android.widget.TextView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class Step3Activity extends AppCompatActivity implements View.OnClickListener {

    Button RecordButton;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String filepath;
    boolean isRecording = false;
    boolean isPlaying = false;
    TextView scoreTextView;
    TextView AITextView;
    TextView USERsttTextView;
    Button playButton;
    Random rand;


    private String historyMsg = " ";

    private String roleplay;







    API_Interface api_interface;

    private static final Map<String, List<String>> roleplayToGoalMap = new HashMap<String, List<String>>() {{
        put("situation1", Arrays.asList("아픈곳 말하기", "이름과 생년월일 말하기", "진료예약 확인하기"));
        put("situation2", Arrays.asList("밥을 잘 먹고있는지 말하기", "나의 알레르기에 대해 말하기", "내가 지금 먹고있는 약이 있는지 말하기"));
    }};




    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step3);

        Intent intent = getIntent();
        roleplay = intent.getStringExtra("roleplay");
        Log.d("roleplay", roleplay);

        Toolbar toolbar = findViewById(R.id.toolbar);

        RecordButton = findViewById(R.id.RecordButton);
        RecordButton.setOnClickListener(this);

        scoreTextView = findViewById(R.id.scoreTextView);
        AITextView = findViewById(R.id.AItext);
        USERsttTextView = findViewById(R.id.Userstt);

        playButton = findViewById(R.id.playbutton); // Add play button in your XML and find it here
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopPlaying();
                } else {
                    startPlaying();
                }
            }
        });

        rand = new Random();

        api_interface = API_Client.getClient().create(API_Interface.class);



    }

    @Override
    public void onClick(View v) {
        if( v== RecordButton) {
            if (isRecording) {
                stopRecording();

            } else {
                startRecording();
            }
        }
    }

    //녹음하는 기능도, 화면을 그려주는것은 아니니까 viewmodel으로 빼낸다든지
    public void startRecording(){
        Log.d("성공", "녹음 시작");

        String filename = String.valueOf(rand.nextInt(1000000));
        Log.d("filename", filename);

        filepath = getExternalFilesDir(null).getAbsolutePath() + "/" +filename +".mp3";
        Log.d("filepath", filepath);



        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(filepath);

        try{
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //이 부분도 마찬가지로 viewmodel
    public void stopRecording(){
        Log.d("성공", "녹음 종료");
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            File recordedFile = new File(filepath);
            Log.d("성공", "파일 전송");

            uploadAudiofile(filepath, roleplay);
        }
    }

    //api 통신하는 부분과 response를 받아서 화면에 그려주는 부분이 혼재함. api 통신 부분(필요하다면 json을 맵핑하는 부분까지도)은 model쪽, 가공하는 부분은 viewmodel쪽, 화면에 그려주는 부분은 view쪽으로 구분하는게 좋지 않을까?
    private void uploadAudiofile(String filepath, String roleplay){
        File file = new File(filepath);
        Log.d("roleplay", roleplay);
        if(!file.exists()){
            Log.e("파일 업로드", "File Does not exist: + filepath");
            return;
        }




        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio_file", file.getName(), requestFile);
        Log.d("파일 업로드", "파일 이름: " + file.getName());
        Log.d("파일 업로드", "파일 경로: " + filepath);


        RequestBody history_msg = RequestBody.create(MediaType.parse("text/plain"), historyMsg);
        Log.d("히스토리 메시지", "히스토리 메시지: " + historyMsg);

        api_interface.lev3_play(roleplay, body, history_msg).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        Log.d("data", data);
                        JSONObject jObject = new JSONObject(data);
                        String jStatus = jObject.getString("status");
                        Log.d("jStatus", jStatus);

                        String sttResult = jObject.getString("stt_result");
                        historyMsg = updateHistory(historyMsg, sttResult);

                        JSONObject jTotalScore = jObject.getJSONObject("total_score");
                        String jPronScore = jTotalScore.getString("pron_score");
                        String jAccurScore = jTotalScore.getString("accuracy_score");
                        String jCompleScore = jTotalScore.getString("completeness_score");
                        String jFluScore = jTotalScore.getString("fluency_score");

                        String scoreText = "정확도: " + jAccurScore + "\n" +
                                "완성도: " + jCompleScore + "\n" +
                                "유창성: " + jFluScore + "\n";

                        scoreTextView.setText(scoreText);

                        String aiResponse = jObject.getString("ai_response");
                        JSONObject aiResponseObject = new JSONObject(aiResponse);
                        String content = aiResponseObject.getString("content");

                        historyMsg = updateHistory(historyMsg, content);

                        AITextView.setText(content);

                        Log.d("TTS", content);

                        TextToSpeech(content);

                        USERsttTextView.setText(sttResult);

                        Log.d("roleplay", roleplay);

                        CheckGoals(roleplay);

                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("결과 : ", "실패");
                t.printStackTrace();
            }
        });

    }

    private void CheckGoals(String roleplay){
        RequestBody requestHistory = RequestBody.create(MediaType.parse("text/plain"), historyMsg);

//        String[] historyArray = historyMsg.split("/");
//        for (String msg : historyArray){
//            String[] parts = msg.split(":", 2);
//            if (parts.length ==2){
//                messages.add(new Message(parts[0], parts[1]));
//            }
//        }
//        Messages requestBody = new Messages(messages);

        api_interface.checkGoals(roleplay, requestHistory).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String data = response.body().string();
                        Log.d("data", data);
                        JSONObject jObject = new JSONObject(data);

                        boolean allGoalsAchieved = true;

                        JSONArray goalList = jObject.getJSONArray("goal_list");

                        List<String> goals = roleplayToGoalMap.get(roleplay);


                        for (int i = 0; i < goalList.length(); i++) {
                            JSONObject goalObject = goalList.getJSONObject(i);
                            String goal = goalObject.getString("goal");
                            boolean accomplished = goalObject.getBoolean("accomplished");

                            if (!accomplished) {
                                allGoalsAchieved = false; // 목표가 달성되지 않으면 false로 설정
                                Log.d("Goal", goal + ": Not Achieved");
                            } else {
                                Log.d("Goal", goal + ": Achieved");
                            }
                        }

                        if (allGoalsAchieved) {
                            showPopup();
                        }


//                        for (int i = 0; i < goalList.length(); i++) {
//                            JSONObject goal = goalList.getJSONObject(i);
//                            String goalName = goal.getString("goal");
//                            boolean accomplished = goal.getBoolean("accomplished");
//                            Log.d("Goal", goalName + ": " + (accomplished ? "Achieved" : "Not Achieved"));
//
//                            if (!accomplished) {
//                                allGoalsAchieved = false;
//                            }
//                        }
//                        if(allGoalsAchieved){
//                            showPopup();
//                        }

                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("결과 : ", "실패");
                t.printStackTrace();
            }
        });
    }

    private String updateHistory(String historyMsg, String content) {
        if (historyMsg == " ") {
            return content;
        } else {
            return historyMsg + "/" + content;
        }
    }

    private void showPopup() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.layout_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button btnExit = popupView.findViewById(R.id.btnExit);
        Button btnCancel = popupView.findViewById(R.id.btnCancel);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(Step3Activity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 필요시 다른 동작 추가
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private void TextToSpeech(String content) {
        Log.d("TTS 요청", "Content: " + content);

        RequestBody responseTxt = RequestBody.create(MediaType.parse("text/plain"), content);
        api_interface.textToSpeech(responseTxt).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // TTS 성공 처리
                    Log.d("TTS 성공", "음성 변환 성공");
                    saveAudioFile(response.body().byteStream(), "tts_audio.mp3");


                    // 여기서 추가적으로 오디오 파일을 재생하는 로직을 추가할 수 있습니다.
                } else {
                    Log.e("TTS 실패", "서버 응답: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TTS 실패", "API 호출 실패", t);
            }
        });
    }
    private void saveAudioFile(InputStream inputStream, String fileName) {
        try {
            File file = new File(getExternalFilesDir(null), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            Log.d("파일 저장", "파일 저장 성공: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("파일 저장 실패", "오류 발생: ", e);
        }
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getExternalFilesDir(null).getAbsolutePath() + "/tts_audio.mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    Log.d("MediaPlayer", "재생 완료");
                }
            });
        } catch (IOException e) {
            Log.e("MediaPlayer 오류", "오류 발생: ", e);
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }






}
