package com.example.haneum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StepThreeAdapter extends RecyclerView.Adapter<StepThreeAdapter.MessageViewHolder> {
    private List<StepThreeMessage> messageList;
    private Context context;


    public StepThreeAdapter(List<StepThreeMessage> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_stepthree, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StepThreeMessage message = messageList.get(position);
        holder.textViewMessage.setText(message.getText());

        if (message.isUser()) {
            holder.imageViewAvatar.setImageResource(R.drawable.step3_user_icon);
            holder.buttonPlay.setVisibility(View.GONE);
        } else {
            holder.imageViewAvatar.setImageResource(R.drawable.step3_ai_icon);
            holder.buttonPlay.setVisibility(View.VISIBLE); // AI 메시지일 경우 음성 재생 버튼 보이기
            holder.buttonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof StepThreeActivity) {
                        String filePath = context.getCacheDir().getAbsolutePath() + "/tts_audio" + position + ".mp3";
                        ((StepThreeActivity) context).startPlaying(getFilePath(position)); // 오디오 파일 재생
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    private String getFilePath(int position) {
        int aiMessageIndex = 0;
        for (int i = 0; i <= position; i++) {
            if (!messageList.get(i).isUser()) {
                aiMessageIndex++;
            }
        }
        return context.getCacheDir().getAbsolutePath() + "/tts_audio" + aiMessageIndex + ".mp3";
    }
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView textViewMessage;
        Button buttonPlay;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            buttonPlay = itemView.findViewById(R.id.buttonPlay);
        }
    }
}