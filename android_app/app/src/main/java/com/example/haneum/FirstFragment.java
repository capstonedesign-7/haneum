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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.io.IOException;

public class FirstFragment extends Fragment implements View.OnClickListener{


    Button temp, temp2;
    String topic;
    String language;
    String situation;
    public static FirstFragment singleton;
    public static FirstFragment newInstance(String situation, String topic){
        if (singleton == null){
            singleton = new FirstFragment();
            Bundle args = new Bundle();
            args.putString("situation", situation);
            args.putString("topic", topic);
            singleton.setArguments(args);
        }
        return singleton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        topic = getArguments().getString("topic","");
        situation = getArguments().getString("situation","");

        View view = inflater.inflate(R.layout.activity_firstfrag, container, false);
        LanguageSet languageSet = new LanguageSet();
        language = languageSet.getLanguage(getActivity());


        if (topic.equals("treatment")){
            TextView tt = (TextView) view.findViewById(R.id.l_topic);
            tt.setText(languageSet.getStringLocal(getActivity(), R.string.topic_treatment, language));

            TextView topic_goal = (TextView) view.findViewById(R.id.l_topic_goal);
            topic_goal.setText(languageSet.getStringLocal(getActivity(), R.string.topic_treatment_goal, language));

        }
        TextView step1_goal = (TextView) view.findViewById(R.id.step1_goal);
        step1_goal.setText(languageSet.getStringLocal(getActivity(), R.string.step1_goal, language));

        TextView step2_goal = (TextView) view.findViewById(R.id.step2_goal);
        step2_goal.setText(languageSet.getStringLocal(getActivity(), R.string.step2_goal, language));

        TextView step1_info = (TextView) view.findViewById(R.id.step1_info);
        step1_info.setText(languageSet.getStringLocal(getActivity(), R.string.step1_info, language));

        TextView step2_info = (TextView) view.findViewById(R.id.step2_info);
        step2_info.setText(languageSet.getStringLocal(getActivity(), R.string.step2_info, language));

        temp = view.findViewById(R.id.step1_start);
        temp.setOnClickListener(this);
        temp.setText(languageSet.getStringLocal(getActivity(), R.string.step1_start, language));
        temp2 = view.findViewById(R.id.step2_start);

        temp2.setOnClickListener(this);
        temp2.setText(languageSet.getStringLocal(getActivity(), R.string.step2_start, language));

        return view;
    }


    @Override
    public void onClick(View v){

        if(v == temp){
            if(topic.equals("treatment")) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StepOneActivity.class);
                intent.putExtra("situation",situation);
                intent.putExtra("topic", "treatment");
                startActivity(intent);
            }
        }else if ( v == temp2){
            Intent intent = new Intent(getActivity().getApplicationContext(), StepTwoActivity.class);
            intent.putExtra("situation", situation);
            intent.putExtra("topic", "treatment");
            startActivity(intent);
        }
    }
}
