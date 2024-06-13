package com.example.haneum;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


public class SettingActivity extends AppCompatActivity {

    String language;
    int locale_number; // 현재 설정되어 있는 언어

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /* Toolbar */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled((false));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* 언어 설정*/
        LanguageSet languageSet = new LanguageSet();
        language = languageSet.getLanguage(this);

        if (language.equals("ko")){
            locale_number = 0;
        }else if(language.equals("vi")){
            locale_number = 1;
        }else{
            locale_number = 0;
        }


        /* Spinner */
        Spinner spinner = (Spinner) findViewById(R.id.selec_language);


        TextView set_language = findViewById(R.id.set_language);
        set_language.setText("Language");

        ArrayList<String> locales = new ArrayList<>();
        locales.add("Korean, 한국어");
        locales.add("Vietnamese, tiếng Việt");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locales);
        spinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        if(sharedPreferences.contains("language")){
            spinner.setSelection(locale_number);
        }else{
            spinner.setSelection(0); // 선택된 item을 보여줌
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != locale_number) { // 선택한 아이템이 내부 저장소에 저장된 언어 설정 값과 다를 때

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if(i == 0){
                        editor.putString("language", "ko");
                    }else if(i == 1){
                        editor.putString("language", "vi");
                    }

                    editor.commit();

                    Intent intent = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    finish();
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner.setSelection(locale_number);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


}