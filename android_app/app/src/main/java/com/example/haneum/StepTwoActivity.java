package com.example.haneum;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class StepTwoActivity extends AppCompatActivity implements View.OnClickListener {

    String filepath;
    RecyclerView vieee;
    StepOneTwoAdapter adapterr;
    ArrayList item;
    String getTopic, getSituation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steptwo);
        getTopic = getIntent().getStringExtra("topic");
        getSituation = getIntent().getStringExtra("situation");

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));


        filepath = getCacheDir().getAbsolutePath();
        vieee = (RecyclerView) findViewById(R.id.steptwo_list);
        vieee.setItemViewCacheSize(20); // 캐시 사이즈 크게 설정해서 재사용 막음..
        adapterr = new StepOneTwoAdapter(this, getTopic, getSituation);

        vieee.setAdapter(adapterr);
        vieee.setLayoutManager(new LinearLayoutManager(this));

        /* adapt data */
        item = new ArrayList<>();
        if (getSituation.equals("hospital") && getTopic.equals("treatment")) {
            item.add(new StepOneTwo_Class("2", "11", "밥은 잘 드시고 계신가요?", "step2_1_audio.mp3", filepath + "/step2_1_record"));
            item.add(new StepOneTwo_Class("2", "12", "혹시 알레르기가 있나요?", "step2_2_audio.mp3", filepath + "/step2_2_record"));
            item.add(new StepOneTwo_Class("2", "13", "가족과 함께 생활하시나요?", "step2_3_audio.mp3", filepath + "/step2_3_record"));
            item.add(new StepOneTwo_Class("2", "14", "언제 가장 많이 아프세요?", "step2_4_audio.mp3", filepath + "/step2_4_record"));
            item.add(new StepOneTwo_Class("2", "15", "언제부터 증상이 있으셨던 거예요?", "step2_5_audio.mp3", filepath + "/step2_5_record"));
            item.add(new StepOneTwo_Class("2", "16", "본인 혈액형이 어떻게 되세요?", "step2_6_audio.mp3", filepath + "/step2_6_record"));
            item.add(new StepOneTwo_Class("2", "17", "최근 잠은 평균 몇 시간 정도 주무셨나요?", "step2_7_audio.mp3", filepath + "/step2_7_record"));
            item.add(new StepOneTwo_Class("2", "18", "꾸준히 하는 운동이 있으신가요?", "step2_8_audio.mp3", filepath + "/step2_8_record"));
            item.add(new StepOneTwo_Class("2", "19", "혹시 알약을 못 드시나요?", "step2_9_audio.mp3", filepath + "/step2_9_record"));
            item.add(new StepOneTwo_Class("2", "20", "또 다른 데, 아픈 곳 있으신가요?", "step2_10_audio.mp3", filepath + "/step2_10_record"));
        }

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

            LanguageSet languageSet = new LanguageSet();
            String language = languageSet.getLanguage(this);

            TextView close = dialogView.findViewById(R.id.close);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
            Button btnExit = dialogView.findViewById(R.id.btnExit);

            close.setText(languageSet.getStringLocal(this, R.string.close, language));
            btnCancel.setText(languageSet.getStringLocal(this, R.string.cancel, language));
            btnExit.setText(languageSet.getStringLocal(this, R.string.exit, language));


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

                    dialog.dismiss();
                    finish(); // 액티비티 종료

                }
            });

            dialog.show();
        }
        return true;
    }
}
