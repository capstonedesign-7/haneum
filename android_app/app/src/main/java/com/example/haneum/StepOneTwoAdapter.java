package com.example.haneum;

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
    String step;
    String sum, comple;
    int step_sum, step_comple;

    @NonNull
    @Override
    public StepOneTwoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sentence = (TextView) itemView.findViewById(R.id.v_sentence);

            record_start = (ImageView) itemView.findViewById(R.id.record_start);
            record_stop = (ImageView) itemView.findViewById(R.id.record_stop);

            audio_start = (ImageView) itemView.findViewById(R.id.audio_start);
            pron_score = (TextView) itemView.findViewById(R.id.v_pron_score);
            accuracy_score = (TextView) itemView.findViewById(R.id.v_accuracy_score);
            comple_score = (TextView) itemView.findViewById(R.id.v_comple_score);
            fluency_score = (TextView) itemView.findViewById(R.id.v_fluency_score);
            words_score = (LinearLayout) itemView.findViewById(R.id.v_words_score);

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

                            record_start.setVisibility(View.GONE);
                            record_stop.setVisibility(View.VISIBLE);

                            int pos = getAdapterPosition();
                            item = step_list.get(pos);

                            String stepNum = item.getStepNum();
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

                            String stepNum = item.getStepNum();



                            if(mediaRecorder != null){
                                mediaRecorder.stop();
                                mediaRecorder.release();
                                mediaRecorder = null;

                                File file = new File(recordFile);

                                API_Connect api_connect = new API_Connect();

                                api_connect.connect(stepNum, file, item.getTextIdx(), "0", new Result_Interface() {

                                    @Override
                                    public void success(Result_Class result) {
                                        Log.d("status", result.getStatus());

                                        pron_score.setText(result.getTotal_score().getPron_score().toString());
                                        accuracy_score.setText(result.getTotal_score().getAccuracy_score().toString());
                                        comple_score.setText(result.getTotal_score().getCompleteness_score().toString());
                                        fluency_score.setText(result.getTotal_score().getFluency_score().toString());

                                        if (stepNum == "2") {
                                            stt_result = (TextView) itemView.findViewById(R.id.v_stt_result);
                                            contents_feedback = (TextView) itemView.findViewById(R.id.v_contents_feedback);

                                            stt_result.setText(result.getStt_result());
                                            contents_feedback.setText(result.getContents_feedback());
                                        }

                                        int words_size = result.getWordsScore_size();
                                        layoutInflater = LayoutInflater.from(context);

                                        for (int i = 0; i < words_size; i++) {
                                            vi[0] = layoutInflater.inflate(R.layout.layout_word, null, false);

                                            String word = result.getWordsScore(i).getWord();
                                            Double score = result.getWordsScore(i).getScore();
                                            String type = result.getWordsScore(i).getType();

                                            TextView words = vi[0].findViewById(R.id.v_www);
                                            words.setText(word);

                                            TextView scores = vi[0].findViewById(R.id.v_nnn);
                                            scores.setText(score.toString());

                                            TextView types = vi[0].findViewById(R.id.v_ttt);
                                            types.setText(type);

                                            words_score.addView(vi[0]);
                                        }

                                        checkContents(stepNum);

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

            sentence.setText(item.getSentence());

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

    public  void checkContents(String stepNum){


        if (stepNum == "1"){
            step = "step1";
            sum = "step1_sum";
            comple = "step1_comple";

        }else if(stepNum == "2"){
            step = "step2";
            sum = "step2_sum";
            comple = "step2_comple";
        }

        if (stepNum == "1"){
            sharedPreferences = context.getSharedPreferences(step, context.MODE_PRIVATE);
            if(sharedPreferences.contains(sum)){
                step_sum = sharedPreferences.getInt(sum, -1);
            }else{
                step_sum = 0;
            }

            if(sharedPreferences.contains(comple)){
                step_comple = sharedPreferences.getInt(comple, -1);
            }else{
                step_comple = 0;
            }
        }else if(stepNum =="2"){
            sharedPreferences = context.getSharedPreferences(step, context.MODE_PRIVATE);
            if(sharedPreferences.contains(sum)){
                step_sum = sharedPreferences.getInt(sum, -1);
            }else{
                step_sum = 0;
            }

            if(sharedPreferences.contains(comple)){
                step_comple = sharedPreferences.getInt(comple, -1);
            }else{
                step_comple = 0;
            }
        }

        if (step_sum == 10-1 && step_comple == 0){

            Handler handler = new Handler();

            handler.postDelayed(new Runnable()
            {
                public void run()
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(sum, 0);
                    editor.putInt(comple, 1);
                    editor.commit();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.layout_popup, null);
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
                            if(directory != null){
                                for (File file : directory ){
                                    file.delete();
                                }
                            }


                            dialog.dismiss();
                            ((Activity)context).finish(); // 액티비티 종료

                        }
                    });

                    dialog.show();
                }
            }, 1500);



        }else if (step_sum <= 0 || step_sum < 10-1){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(sum, step_sum + 1);
            editor.commit();

        }
    }
}
