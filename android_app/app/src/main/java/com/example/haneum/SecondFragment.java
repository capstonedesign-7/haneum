package com.example.haneum;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment implements View.OnClickListener{
    Button temp;
    public static SecondFragment singleton;

    public static SecondFragment newInstance(){
        if (singleton == null){
            singleton = new SecondFragment();
        }
        return singleton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_secondfrag, container, false);

        temp = view.findViewById(R.id.startbutton);
        temp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){

        if(v == temp){
            Intent NewActivity = new Intent(getActivity().getApplicationContext(), Step3Activity.class);
            startActivity(NewActivity);
        }
    }
}
