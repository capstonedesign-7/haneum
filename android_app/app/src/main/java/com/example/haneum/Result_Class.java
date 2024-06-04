package com.example.haneum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result_Class {

    @SerializedName("status")
    private String status;

    @SerializedName("total_score")
    private TotalScore total_score;
    @SerializedName("words_score")
    private ArrayList<WordsScore> words_score;

    @Expose
    @SerializedName("stt_result")
    private String stt_result;

    @Expose
    @SerializedName("contents_feedback")
    private String contents_feedback;


    public String getStatus(){
        return status;
    }

    public TotalScore getTotal_score(){
        return total_score;
    }

    public int getWordsScore_size(){
        return words_score.size();
    }
    public WordsScore getWordsScore(int id){
        return words_score.get(id);
    }

    public String getStt_result(){
        return stt_result;
    }

    public String getContents_feedback(){
        return contents_feedback;
    }

}


class TotalScore{
    @SerializedName("pron_score")
    private double pron_score;

    @SerializedName("accuracy_score")
    private double accuracy_score;

    @SerializedName("completeness_score")
    private double completeness_score;

    @SerializedName("fluency_score")
    private double fluency_score;

    public Double getPron_score(){
        return pron_score;
    }
    public Double getAccuracy_score(){
        return accuracy_score;
    }
    public Double getCompleteness_score(){
        return completeness_score;
    }
    public Double getFluency_score(){
        return fluency_score;
    }
}

class WordsScore{
    @SerializedName("word")
    private String word;

    @SerializedName("score")
    private double score;

    @SerializedName("type")
    private String type;

    public String getWord(){
        return word;
    }

    public Double getScore(){
        return score;
    }

    public String getType(){
        return type;
    }
}
