package com.example.haneum;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String language;
    SharedPreferences sharedPreferences;
    int step1_state, step2_state, step3_state;

    ImageView step1_i;
    ImageView step2_i;
    ImageView step3_i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* 권한 요청 */
        ActivityCompat.requestPermissions(this , new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},0);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));

        /* 언어 설정 */
        LanguageSet languageSet = new LanguageSet();
        language = languageSet.getLanguage(this);

        TextView situation = findViewById(R.id.situation);
        TextView temp = findViewById(R.id.temp);

        situation.setText(languageSet.getStringLocal(this, R.string.situation, language));
        temp.setText(languageSet.getStringLocal(this, R.string.later, language));

        /* 병원 */
        LinearLayout hospital = findViewById(R.id.l_hospital);
        hospital.setOnClickListener(this);

        ImageView hos_image = hospital.findViewById(R.id.situation_image);
        TextView hos_name = hospital.findViewById(R.id.situation_name);
        TextView hos_step1 = hospital.findViewById(R.id.step1);
        TextView hos_step2 = hospital.findViewById(R.id.step2);
        TextView hos_step3 = hospital.findViewById(R.id.step3);

        step1_i = hospital.findViewById(R.id.i_step1);
        step2_i = hospital.findViewById(R.id.i_step2);
        step3_i = hospital.findViewById(R.id.i_step3);

        hos_image.setImageResource(R.drawable.hospital);
        hos_name.setText(languageSet.getStringLocal(this, R.string.hospital, language));
        hos_step1.setText(languageSet.getStringLocal(this, R.string.step1, language));
        hos_step2.setText(languageSet.getStringLocal(this, R.string.step2, language));
        hos_step3.setText(languageSet.getStringLocal(this, R.string.step3, language));

        sharedPreferences = getSharedPreferences("step", MODE_PRIVATE);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.l_hospital){
            Intent intent = new Intent(getApplicationContext(), TopicActivity.class);
            intent.putExtra("situation", "hospital");
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sharedPreferences.contains("hospital_step1_comple")){
            step1_state = sharedPreferences.getInt("hospital_step1_comple", 0);
        }else{
            step1_state = 0;
        }

        if(sharedPreferences.contains("hospital_step2_comple")){
            step2_state = sharedPreferences.getInt("hospital_step2_comple", 0);
        }else{
            step2_state = 0;
        }

        if(sharedPreferences.contains("hospital_step3_comple")){
            step3_state = sharedPreferences.getInt("hospital_step3_comple", 0);
        }else{
            step3_state = 0;
        }

        if(step1_state == 1){
            step1_i.setImageResource(R.drawable.blue_circle);
        }else {
            step1_i.setImageResource(R.drawable.red_circle);
        }

        if(step2_state == 1){
            step2_i.setImageResource(R.drawable.blue_circle);
        }else {
            step2_i.setImageResource(R.drawable.red_circle);
        }

        if(step3_state == 2){
            step3_i.setImageResource(R.drawable.blue_circle);
        }else {
            step3_i.setImageResource(R.drawable.red_circle);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.toolbar_setting) {
            Intent NewActivity = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(NewActivity);
        }
        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        ExitPopup();
    }
    private void ExitPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_exit, null);
        builder.setView(dialogView);

        LanguageSet languageSet = new LanguageSet();
        language = languageSet.getLanguage(this);

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
                finish(); // 앱 종료
            }
        });

        dialog.show(); // 팝업 출력
    }


}