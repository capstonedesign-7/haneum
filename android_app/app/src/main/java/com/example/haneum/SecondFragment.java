package com.example.haneum;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;

public class SecondFragment extends Fragment implements View.OnClickListener{
    Button situ1;
    Button situ2;
    public static SecondFragment singleton;

    String topic, situation;
    String language;
    public static SecondFragment newInstance( String situation,String topic){
        if (singleton == null){
            singleton = new SecondFragment();
            Bundle args = new Bundle();
            args.putString("topic", topic);
            args.putString("situation", situation);
            singleton.setArguments(args);
        }
        return singleton;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        topic = getArguments().getString("topic","");
        situation = getArguments().getString("situation","");

        View view = inflater.inflate(R.layout.activity_secondfrag, container, false);
        LanguageSet languageSet = new LanguageSet();
        language = languageSet.getLanguage(getActivity());
        if (topic.equals("treatment")){
            TextView tt = (TextView) view.findViewById(R.id.l_topic);
            tt.setText(languageSet.getStringLocal(getActivity(), R.string.topic_treatment, language));
            TextView goal1 = (TextView) view.findViewById(R.id.goal1);
            TextView goal2 = (TextView) view.findViewById(R.id.goal2);

            goal1.setText(languageSet.getStringLocal(getActivity(), R.string.topic_treatment_3_goal1, language));
            goal2.setText(languageSet.getStringLocal(getActivity(), R.string.topic_treatment_3_goal2, language));
        }

        TextView step3_info = view.findViewById(R.id.step3_info);
        step3_info.setText(languageSet.getStringLocal(getActivity(), R.string.step3_info, language));

        situ1 = view.findViewById(R.id.step3_start_1);
        situ1.setOnClickListener(this);
        situ1.setText(languageSet.getStringLocal(getActivity(), R.string.step3_start, language));
        situ2 = view.findViewById(R.id.step3_start_2);
        situ2.setOnClickListener(this);
        situ2.setText(languageSet.getStringLocal(getActivity(), R.string.step3_start, language));


        return view;
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(getActivity().getApplicationContext(), Step3Activity.class);

        if (v == situ1) {
            if(topic.equals("treatment")) {
                intent.putExtra("situation", situation);
                intent.putExtra("topic",topic);
                intent.putExtra("roleplay", "situation1");
            }
             // situ1 버튼을 클릭했을 때 전달할 인자
        } else if (v == situ2) {
            if (topic.equals("treatment")) {
                intent.putExtra("situation", situation);
                intent.putExtra("topic",topic);
                intent.putExtra("roleplay", "situation2"); // situ2 버튼을 클릭했을 때 전달할 인자
            }
        }

        startActivity(intent);
    }

}
