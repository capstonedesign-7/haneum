package com.example.haneum;

import android.annotation.SuppressLint;
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
    Button situ1;
    Button situ2;
    public static SecondFragment singleton;

    public static SecondFragment newInstance(){
        if (singleton == null){
            singleton = new SecondFragment();
        }
        return singleton;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_secondfrag, container, false);

        situ1 = view.findViewById(R.id.startbutton1);
        situ1.setOnClickListener(this);
        situ2 = view.findViewById(R.id.startbutton2);
        situ2.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(getActivity().getApplicationContext(), Step3Activity.class);

        if (v == situ1) {
            intent.putExtra("roleplay", "situation1"); // situ1 버튼을 클릭했을 때 전달할 인자
        } else if (v == situ2) {
            intent.putExtra("roleplay", "situation2"); // situ2 버튼을 클릭했을 때 전달할 인자
        }

        startActivity(intent);
    }

}
