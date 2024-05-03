package com.example.haneum;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

public class StepOneActivity extends AppCompatActivity  implements View.OnClickListener{

    Button btnstart, btnstop;
    MediaRecorder mediaRecorder;
    String filepath;

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


        filepath = getCacheDir().getAbsolutePath();
        filepath += "/audiorecordtest2.mp3";
        Log.d("filepath",filepath);



    }
    public void onClick(View v){
        if(v == btnstart){
            Log.d("성공", "hi2");
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile(filepath);
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
            }
        }

    }
}
