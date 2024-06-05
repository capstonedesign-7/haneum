package com.example.haneum;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

public class StepOneActivity extends AppCompatActivity  implements View.OnClickListener{

    String filepath;
    RecyclerView vieee;
    StepOneTwoAdapter adapterr;
    ArrayList item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepone);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));



        filepath = getCacheDir().getAbsolutePath();

        vieee = (RecyclerView) findViewById(R.id.stepone_list);
        vieee.setItemViewCacheSize(20); // 캐시 사이즈 크게 설정해서 재사용 막음..
        adapterr = new StepOneTwoAdapter();
        //adapter = new StepOneTwoAdapter();

        vieee.setAdapter(adapterr);
        vieee.setLayoutManager(new LinearLayoutManager(this));

        /* adapt data */
        item = new ArrayList<>();


        item.add(new StepOneTwo_Class("1","1","코가막히고 목이 따끔거리고 기침이 나요", "step1_1_audio.mp3",filepath+"/step1_1_record"));
        item.add(new StepOneTwo_Class("1","2","언제쯤 나아서 학교에 갈 수 있나요?", "step1_2_audio.mp3",filepath+"/step1_2_record"));
        item.add(new StepOneTwo_Class("1","3","전에도 갑자기 아팠던 적이 있어요.", "step1_3_audio.mp3",filepath+"/step1_3_record"));
        item.add(new StepOneTwo_Class("1","4","발목이 시큰거리고 빨갛게 부었어요.", "step1_4_audio.mp3",filepath+"/step1_4_record"));
        item.add(new StepOneTwo_Class("1","5","눈이 건조하고 자주 충혈되는 것 같아요.", "step1_5_audio.mp3",filepath+"/step1_5_record"));
        item.add(new StepOneTwo_Class("1","6","약은 매일 식후 삼십 분 후에 드세요.", "step1_6_audio.mp3",filepath+"/step1_6_record"));
        item.add(new StepOneTwo_Class("1","7","치아 스케일링을 하러 왔어요.", "step1_7_audio.mp3",filepath+"/step1_7_record"));
        item.add(new StepOneTwo_Class("1","8","어제 저녁부터 머리가 울리고 열이 나요", "step1_8_audio.mp3",filepath+"/step1_8_record"));
        item.add(new StepOneTwo_Class("1","9","땀이 날 정도로 운동을 하면 안됩니다.", "step1_9_audio.mp3",filepath+"/step1_9_record"));
        item.add(new StepOneTwo_Class("1","10","영양제나 비타민을 먹어도 되나요?", "step1_10_audio.mp3",filepath+"/step1_10_record"));


        adapterr.setItemList(item);


    }
    public void onClick(View v){

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.toolbar_exit) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.layout_exit, null);
            builder.setView(dialogView);

            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
            Button btnExit = dialogView.findViewById(R.id.btnExit);

            final AlertDialog dialog = builder.create();
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 캐시 지우기
                    File[] directory = getCacheDir().listFiles();
                    if(directory != null){
                        for (File file : directory ){
                            file.delete();
                        }
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("step1_sum", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("step1_sum", 0);
                    editor.commit();
                    dialog.dismiss();
                    finish(); // 액티비티 종료
                }
            });

            dialog.show();
            // 여기서 팝업 띄우기

        }
        return true;
    }

}
