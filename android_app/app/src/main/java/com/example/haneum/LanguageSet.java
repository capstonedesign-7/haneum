package com.example.haneum;

import static android.content.Context.MODE_PRIVATE;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageSet {

    public static String getLanguage(Activity context){
        String lang;
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        if(sharedPreferences.contains("language")){
            //locale_number = sharedPreferences.getInt("language", -1);
            lang = sharedPreferences.getString("language", "");
        }else{
            //locale_number = 0;
            lang = "ko";
        }
        return lang;
    }
    public static String getStringLocal(Activity context, int resId, String locale){
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(resId);
    }
}
