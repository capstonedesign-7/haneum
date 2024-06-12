package com.example.haneum;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class StepOneTwoAdapter extends RecyclerView.Adapter<StepOneTwoAdapter.ViewHolder> {
    private ArrayList<StepOneTwo_Class> step_list = new ArrayList<>();
    static int lock  = 0;
    Context context;
    SharedPreferences sharedPreferences;
    String lan;
    String sum, comple, now, step;
    int step_sum, step_comple;
    private Activity activity;
    String language;
    private String getTopic, getSituation;
    public StepOneTwoAdapter(Activity activity, String getTopic, String getSituation){
        this.activity=activity;
        this.getTopic = getTopic;
        this.getSituation = getSituation;


    }


    @NonNull
    @Override
    public StepOneTwoAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_stepone, parent, false);
            context = parent.getContext();

            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_steptwo, parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull StepOneTwoAdapter.ViewHolder holder, int position) {
        holder.onBind(step_list.get(position));

    }

    public void setItemList(ArrayList<StepOneTwo_Class> list){
        this.step_list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (step_list.get(position).getStepNum() == "1") {
            return 1;
        }
        else if (step_list.get(position).getStepNum() == "1") {
            return 2;
        }else{
            return 0;
        }

    }
    @Override
    public int getItemCount() {
        return step_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView sentence, pron_score, accuracy_score, comple_score, fluency_score;
        ImageView record_start, record_stop, audio_start;
        LinearLayout words_score;
        TextView contents_feedback, stt_result;

        String recordFile;
        MediaRecorder mediaRecorder;

        StepOneTwo_Class item;
        LayoutInflater layoutInflater;

        String stepNum;
        int t;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LanguageSet languageSet = new LanguageSet();
            language = languageSet.getLanguage(activity);
            Log.d("language", language);


            TextView sen = itemView.findViewById(R.id.sentence_name);
            if (sen != null ) {
                sen.setText(languageSet.getStringLocal(activity, R.string.sentence, language));
            }

            TextView que = itemView.findViewById(R.id.question_name);
            if(que != null){
                que.setText(languageSet.getStringLocal(activity, R.string.question, language));
            }

            TextView shadow = itemView.findViewById(R.id.shadow_speaking);
            if(shadow != null) {
                shadow.setText(languageSet.getStringLocal(activity, R.string.shadow_speaking, language));
            }

            LinearLayout ll_result = itemView.findViewById(R.id.ll_result);


            TextView ans_ing = itemView.findViewById(R.id.answering);
            if(ans_ing != null){
                ans_ing.setText(languageSet.getStringLocal(activity, R.string.answering, language));
            }
            TextView ans = itemView.findViewById(R.id.answer);
            if(ans != null){
                ans.setText(languageSet.getStringLocal(activity, R.string.answer, language));
            }
            Log.d("error1", "여기서 오류");
            TextView evaluation = ll_result.findViewById(R.id.evaluation);
            evaluation.setText(languageSet.getStringLocal(activity, R.string.evaluation, language));

            TextView compre = ll_result.findViewById(R.id.comprehensive_score);
            compre.setText(languageSet.getStringLocal(activity, R.string.comprehensive_score, language));
            Log.d("error1", "여기서 오류2");
            TextView acc = ll_result.findViewById(R.id.accuracy);
            acc.setText(languageSet.getStringLocal(activity, R.string.accuracy, language));

            TextView com = ll_result.findViewById(R.id.completeness);
            com.setText(languageSet.getStringLocal(activity, R.string.completeness, language));

            TextView flu = ll_result.findViewById(R.id.fluency);
            flu.setText(languageSet.getStringLocal(activity, R.string.fluency, language));

            TextView wbw = ll_result.findViewById(R.id.wordbyword_score);
            wbw.setText(languageSet.getStringLocal(activity, R.string.wordbyword_score, language));

            TextView pha = ll_result.findViewById(R.id.phrase);
            pha.setText(languageSet.getStringLocal(activity, R.string.phrase, language));

            TextView sco = ll_result.findViewById(R.id.score);
            sco.setText(languageSet.getStringLocal(activity, R.string.score, language));

            TextView res = ll_result.findViewById(R.id.result);
            res.setText(languageSet.getStringLocal(activity, R.string.result, language));

            Log.d("error1", "여기서 오류3");

            sentence = (TextView) itemView.findViewById(R.id.v_sentence);

            record_start = (ImageView) itemView.findViewById(R.id.record_start);
            record_stop = (ImageView) itemView.findViewById(R.id.record_stop);

            audio_start = (ImageView) itemView.findViewById(R.id.audio_start);

            pron_score = (TextView) ll_result.findViewById(R.id.v_pron_score);
            accuracy_score = (TextView) ll_result.findViewById(R.id.v_accuracy_score);
            comple_score = (TextView) ll_result.findViewById(R.id.v_comple_score);
            fluency_score = (TextView) ll_result.findViewById(R.id.v_fluency_score);
            words_score = (LinearLayout) ll_result.findViewById(R.id.v_words_score);

            audio_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    item = step_list.get(pos);
                    playAssetSound(context, item.getAudiofile());
                }
            });

            record_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (lock == 0){
                            lock = 1;

                            ll_result.setVisibility(View.GONE);
                            record_start.setVisibility(View.GONE);
                            record_stop.setVisibility(View.VISIBLE);

                            int pos = getAdapterPosition();
                            item = step_list.get(pos);

                            stepNum = item.getStepNum();
                            pron_score.setText("");
                            accuracy_score.setText("");
                            comple_score.setText("");
                            fluency_score.setText("");
                            words_score.removeAllViews();

                            if(stepNum == "2" && stt_result != null && contents_feedback !=null){
                                stt_result.setText("");
                                contents_feedback.setText("");
                            }

                            if(pos != RecyclerView.NO_POSITION) {
                                Log.d("sss", step_list.get(pos).getAudiofile());

                                Long datetime = System.currentTimeMillis();
                                String time = datetime.toString();
                                recordFile = item.getRecordfile() + time + ".mp3";

                                //record_start
                                mediaRecorder = new MediaRecorder();
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                                mediaRecorder.setOutputFile(recordFile);

                                try{
                                    mediaRecorder.prepare();
                                    mediaRecorder.start();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
            });


            record_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final View[] vi = {view};
                        if (lock ==1) {
                            record_start.setVisibility(View.VISIBLE);
                            record_stop.setVisibility(View.GONE);

                            stepNum = item.getStepNum();

                            if(mediaRecorder != null){
                                mediaRecorder.stop();
                                mediaRecorder.release();
                                mediaRecorder = null;

                                File file = new File(recordFile);

                                API_Connect api_connect = new API_Connect();

                                if (language.equals("ko")){
                                    lan = "0";
                                }else if(language.equals("vi")){
                                    lan = "1";
                                }
                                Log.d("language", lan);
                                api_connect.connect(context, stepNum, file, item.getTextIdx(), lan, new Result_Interface() {

                                    @Override
                                    public void success(Result_Class result) {
                                        Log.d("status", result.getStatus());
                                        Double p_score = Math.round(result.getTotal_score().getPron_score()*100)/100.0;
                                        Double a_score = Math.round(result.getTotal_score().getAccuracy_score()*100)/100.0;
                                        Double c_score = Math.round(result.getTotal_score().getCompleteness_score()*100)/100.0;
                                        Double f_score = Math.round(result.getTotal_score().getFluency_score()*100)/100.0;
                                        pron_score.setText(Double.toString(p_score));
                                        accuracy_score.setText(Double.toString(a_score));
                                        comple_score.setText(Double.toString(c_score));
                                        fluency_score.setText(Double.toString(f_score));

                                        if (stepNum == "2") {
                                            stt_result = (TextView) itemView.findViewById(R.id.v_stt_result);
                                            contents_feedback = (TextView) ll_result.findViewById(R.id.v_contents_feedback);
//
                                            stt_result.setText(result.getStt_result());
                                            contents_feedback.setText(result.getContents_feedback());
                                        }

                                        int words_size = result.getWordsScore_size();
                                        layoutInflater = LayoutInflater.from(context);

                                        for (int i = 0; i < words_size; i++) {
                                            vi[0] = layoutInflater.inflate(R.layout.layout_word, null, false);

                                            String word = result.getWordsScore(i).getWord();
                                            Double score = result.getWordsScore(i).getScore();
                                            score = Math.round(score*100)/100.0;
                                            String type = result.getWordsScore(i).getType();

                                            TextView words = vi[0].findViewById(R.id.v_www);
                                            words.setText(word);

                                            TextView scores = vi[0].findViewById(R.id.v_nnn);
                                            scores.setText(score.toString());

                                            TextView types = vi[0].findViewById(R.id.v_ttt);

                                            if (type.equals("None")){
                                                t = R.string.none;
                                            }else if(type.equals("Omission")){
                                                t = R.string.omission;
                                            }
                                            else if(type.equals("Insertion")){
                                                t = R.string.insertion;
                                            }
                                            else if(type.equals("Mispronunciation")){
                                                t = R.string.mispronunciation;
                                            }
                                            else if(type.equals("UnexpectedBreak")){
                                                t = R.string.unexpectedbreak;
                                            }
                                            else if(type.equals("MissingBreak")){
                                                t = R.string.missingbreak;
                                            }
                                            else if(type.equals("Monotone")){
                                                t = R.string.monotone;
                                            }

                                            types.setText(languageSet.getStringLocal(activity, t, language));

                                            words_score.addView(vi[0]);
                                        }
                                        ll_result.setVisibility(View.VISIBLE);
                                        checkContents(getSituation, getTopic, stepNum, item.getTextIdx());

                                    }
                                    @Override
                                    public void failure(Throwable t) {
                                        // Display error
                                        Log.d("error", "result_interface error");
                                    }

                                });

                            }

                            lock = 0;
                        }
                    }
            });



        }

        void onBind(StepOneTwo_Class item){
            Log.d("error1", "여기서 오류5");
            sentence.setText(item.getSentence());
            Log.d("error1", "여기서 오류6");
        }


    }

    public static void playAssetSound(Context context, String soundFileName) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();

            AssetFileDescriptor descriptor = context.getAssets().openFd(soundFileName);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void checkContents(String getSituation, String getTopic, String stepNum, String textIdx){


        if (stepNum == "1"){
            sum = getSituation+"_"+getTopic+"_step1_sum";
            now = getSituation+"_"+getTopic+"_step1_"+textIdx;
            comple = getSituation+"_"+getTopic+"_step1_comple";
            step = getSituation + "_step1_comple";

        }else if(stepNum == "2"){
            sum = getSituation+"_"+getTopic+"_step2_sum";
            now = getSituation+"_"+getTopic+"_step2_"+textIdx;
            comple = getSituation+"_"+getTopic+"_step2_comple";
            step = getSituation + "_step2_comple";
        }
        int now_state;
        int sum_state;
        int comple_state;
        int step_state;

        sharedPreferences =  context.getSharedPreferences("step", MODE_PRIVATE);

        if(sharedPreferences.contains(now)){
            now_state = sharedPreferences.getInt(now, 0);
        }else{
            now_state = 0;
        }
        if(sharedPreferences.contains(sum)){
            sum_state = sharedPreferences.getInt(sum, 0);
        }else{
            sum_state = 0;
        }
        if(sharedPreferences.contains(comple)){
            comple_state = sharedPreferences.getInt(comple, 0);
        }else{
            comple_state = 0;
        }

        if(sharedPreferences.contains(step)){
            step_state = sharedPreferences.getInt(step, 0);
        }else{
            step_state = 0;
        }

        Log.d("comle", Integer.toString(comple_state));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(comple_state == 0) {
            if (now_state == 0) {
                editor.putInt(now, 1);
                editor.putInt(sum, sum_state + 1);
                editor.commit();



                if( sum_state+1 == 10){
                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        public void run() {

                            editor.putInt(comple, 1);
                            editor.putInt(step, step_state +1);
                            editor.commit();

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View dialogView = inflater.inflate(R.layout.layout_complete, null);
                            builder.setView(dialogView);

                            Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                            Button btnExit = dialogView.findViewById(R.id.btnExit);

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

                                    // 캐시 지우기
                                    File[] directory = context.getCacheDir().listFiles();
                                    if (directory != null) {
                                        for (File file : directory) {
                                            file.delete();
                                        }
                                    }


                                    dialog.dismiss();
                                    ((Activity) context).finish(); // 액티비티 종료

                                }
                            });

                            dialog.show();
                        }
                    }, 2000);
                }
            }


        }
    }
}
