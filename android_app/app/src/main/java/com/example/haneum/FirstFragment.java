package com.example.haneum;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class FirstFragment extends Fragment implements View.OnClickListener{


    Button temp, temp2;


    public static FirstFragment singleton;
    public static FirstFragment newInstance(){
        if (singleton == null){
            singleton = new FirstFragment();
        }
        return singleton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_firstfrag, container, false);

        temp = view.findViewById(R.id.button3);
        temp.setOnClickListener(this);

        temp2 = view.findViewById(R.id.step2_start);
        temp2.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v){

        if(v == temp){
            Intent NewActivity = new Intent(getActivity().getApplicationContext(), StepOneActivity.class);
            startActivity(NewActivity);
            Log.d("temp", "temp start");
        }else if ( v == temp2){
            Intent NewActivity = new Intent(getActivity().getApplicationContext(), StepTwoActivity.class);
            startActivity(NewActivity);
            Log.d("temp2", "temp2 start");
        }
    }
}
