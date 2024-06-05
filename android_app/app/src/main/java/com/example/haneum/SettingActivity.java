package com.example.haneum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    Button ReturnBtn;
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
        //setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)   // R.id.home

        /* Spinner */

        /* 내부 저장소 값 불러와야 함*/
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        if(sharedPreferences.contains("language")){
            locale_number = sharedPreferences.getInt("language", -1);
        }else{
            locale_number = 0;
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        String lan;
        if (locale_number == 0){
            lan = "ko";
        }else if(locale_number==1){
            lan = "vi";
        }else{
            lan = "ko";
        }

        TextView textView = findViewById(R.id.textView3);
        textView.setText(getStringByLocal(this,R.string.test_text, lan));

        TextView textView1 = findViewById(R.id.textView);
        textView1.setText(getStringByLocal(this, R.string.lang_set, lan));

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(getStringByLocal(this, R.string.app_info, lan));


        ArrayList<String> locales = new ArrayList<>();
        locales.add(getStringByLocal(this, R.string.kor, lan));
        locales.add(getStringByLocal(this, R.string.vie, lan));

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locales);

        spinner.setAdapter(adapter);

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
                    editor.putInt("language", i);
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

    /* Toolbar Test */

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);

        MenuItem item = menu.findItem(R.id.toolbar_setting);
        item.setVisible(false);

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public static String getStringByLocal(Activity context, int resId, String locale){
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(resId);
    }
}