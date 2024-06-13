package com.example.haneum;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageSet {

    public static String getLanguage(Activity context){
        String lang;

        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        if(sharedPreferences.contains("language")){
            lang = sharedPreferences.getString("language", ""); // 언어 설정 불러오기
        }else{
            lang = "ko"; // 기본 값 = 한국어
        }
        return lang;
    }
    public static String getStringLocal(Activity context, int resId, String locale){
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(resId);
    }
}
