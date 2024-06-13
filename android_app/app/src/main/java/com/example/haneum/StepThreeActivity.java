package com.example.haneum;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.widget.TextView;
import android.media.MediaRecorder;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


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

public class StepThreeActivity extends AppCompatActivity implements View.OnClickListener {

    Button RecordButton;
    Button playButton;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    boolean isRecording = false;
    boolean isPlaying = false;
    TextView AITextView, USERsttTextView;
    TextView v_accuracy, v_comple, v_fluency;
    TextView accu, comple, flu;
    SharedPreferences sharedPreferences;
    int lock = 1;
    int comple_state, role_state;
    String getSituation, getTopic;
    String language;
    String filepath;
    API_Interface api_interface;

    private int ttsaudioindex = 0;
    private String historyMsg = " ";
    private String roleplay;
    private RecyclerView recyclerView;
    private StepThreeAdapter adapter;
    private List<StepThreeMessage> messageList;


    private static final Map<String, List<String>> roleplayToGoalMap = new HashMap<String, List<String>>() {{
        put("situation1", Arrays.asList("아픈곳 말하기", "이름과 생년월일 말하기", "진료예약 확인하기"));
        put("situation2", Arrays.asList("밥을 잘 먹고있는지 말하기", "나의 알레르기에 대해 말하기", "내가 지금 먹고있는 약이 있는지 말하기"));
    }};

    public StepThreeActivity() {
    }


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepthree);

        Intent intent = getIntent();
        roleplay = intent.getStringExtra("roleplay");
        getSituation = intent.getStringExtra("situation");
        getTopic = intent.getStringExtra("topic");

        sharedPreferences =  getSharedPreferences("step", MODE_PRIVATE);

        v_comple = findViewById(R.id.v_comple_score);
        v_fluency = findViewById(R.id.v_fluency_score);
        v_accuracy = findViewById(R.id.v_accuracy_score);

        accu = findViewById(R.id.accuracy);
        comple = findViewById(R.id.completeness);
        flu = findViewById(R.id.fluency);

        LanguageSet languageSet = new LanguageSet();
        language = languageSet.getLanguage(this);

        accu.setText(languageSet.getStringLocal(this, R.string.accuracy, language));
        comple.setText(languageSet.getStringLocal(this, R.string.completeness, language));
        flu.setText(languageSet.getStringLocal(this, R.string.fluency, language));

        Log.d("roleplay", roleplay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));

        RecordButton = findViewById(R.id.RecordButton);
        RecordButton.setOnClickListener(this);

        AITextView = findViewById(R.id.AItext);
        AITextView.setText(languageSet.getStringLocal(this, R.string.step3_ai_start, language));
        USERsttTextView = findViewById(R.id.Userstt);

        playButton = findViewById(R.id.playbutton);
        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopPlaying();
            } else {
                if (lock == 0) {
                    String currentfilename = "tts_audio" + ttsaudioindex + ".mp3";
                    File ttsfile = new File(getCacheDir(), currentfilename);
                    if (ttsfile.exists() && ttsfile.getName().equals(currentfilename)) {
                        startPlaying(ttsfile.getAbsolutePath());
                        lock = 1;
                    }
                }else{
                    Toast.makeText(StepThreeActivity.this, "최신 오디오 파일이 준비되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        api_interface = API_Client.getClient().create(API_Interface.class);

        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StepThreeAdapter(messageList, this);
        recyclerView.setAdapter(adapter);



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

    public void startRecording(){
        Log.d("성공", "녹음 시작");
        Long datetime = System.currentTimeMillis();
        String time = datetime.toString();
        filepath = getCacheDir().getAbsolutePath() + "/" +time +".mp3";
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
            RecordButton.setBackgroundResource(R.drawable.record_stop);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void stopRecording(){
        Log.d("성공", "녹음 종료");
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            RecordButton.setBackgroundResource(R.drawable.record_start);
            uploadAudiofile(filepath, roleplay);
        }
    }


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
                        addMessage(sttResult, true);

                        JSONObject jTotalScore = jObject.getJSONObject("total_score");
                        String jPronScore = jTotalScore.getString("pron_score");
                        String jAccurScore = jTotalScore.getString("accuracy_score");
                        String jCompleScore = jTotalScore.getString("completeness_score");
                        String jFluScore = jTotalScore.getString("fluency_score");


                        v_accuracy.setText(String.format("%.2f", Double.parseDouble(jAccurScore)));
                        v_comple.setText(String.format("%.2f", Double.parseDouble(jCompleScore)));
                        v_fluency.setText(String.format("%.2f", Double.parseDouble(jFluScore)));

                        String aiResponse = jObject.getString("ai_response");
                        JSONObject aiResponseObject = new JSONObject(aiResponse);
                        String content = aiResponseObject.getString("content");

                        TextToSpeech(content);
                        historyMsg = updateHistory(historyMsg, content);

                        AITextView.setText(content);
                        addMessage(content, false);

                        Log.d("TTS", content);
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
                            String step3_role = getSituation+"_"+getTopic+"_step3_"+roleplay;
                            String step3_comple = getSituation+"_step3_comple";

                            if(sharedPreferences.contains(step3_role)){
                                role_state = sharedPreferences.getInt(step3_role, 0);
                            }else{
                                role_state = 0;
                            }

                            if(sharedPreferences.contains(step3_comple)){
                                comple_state = sharedPreferences.getInt(step3_comple, 0);
                            }else{
                                comple_state = 0;
                            }

                            if ( role_state == 0){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt(step3_role, 1);
                                editor.putInt(step3_comple, comple_state + 1);
                                editor.commit();
                            }

                            showPopup();
                        }

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
        View popupView = inflater.inflate(R.layout.layout_complete, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(popupView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        LanguageSet languageSet = new LanguageSet();
        language = languageSet.getLanguage(this);

        TextView close = popupView.findViewById(R.id.complete);
        Button btnCancel = popupView.findViewById(R.id.btnCancel);
        Button btnExit = popupView.findViewById(R.id.btnExit);

        close.setText(languageSet.getStringLocal(this, R.string.close, language));
        btnCancel.setText(languageSet.getStringLocal(this, R.string.show, language));
        btnExit.setText(languageSet.getStringLocal(this, R.string.exit, language));

        btnExit.setOnClickListener(v -> {
            alertDialog.dismiss();
            Intent intent = new Intent(StepThreeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }
    private void TextToSpeech(String content) {
        Log.d("TTS 요청", "Content: " + content);
        RequestBody responseTxt = RequestBody.create(MediaType.parse("text/plain"), content);
        api_interface.textToSpeech(responseTxt).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ttsaudioindex++;
                    Log.d("TTS 성공", "음성 변환 성공");
                    saveAudioFile(response.body().byteStream(), "tts_audio" + ttsaudioindex + ".mp3");
                    lock = 0;
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
            File file = new File(getCacheDir(), fileName);
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

    public void startPlaying(String filepath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                Log.d("MediaPlayer", "재생 완료");
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

    private void addMessage(String text, boolean isUser) {
        messageList.add(new StepThreeMessage(text, isUser));
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        File[] directory = getCacheDir().listFiles();
        if(directory != null) {
            for (File file : directory) {
                file.delete();
            }
        }
    }
}
