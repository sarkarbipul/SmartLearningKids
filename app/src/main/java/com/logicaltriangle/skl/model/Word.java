package com.logicaltriangle.skl.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Word {
    @SerializedName("w_id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "w_id")
    private int wordId;

    @SerializedName("item_id")
    @ColumnInfo(name = "item_id")
    private int itemId;

    @SerializedName("w_name_1")
    @ColumnInfo(name = "w_name_1")
    private String wordName1;

    @SerializedName("w_name_2")
    @ColumnInfo(name = "w_name_2")
    private String wordName2;

    @SerializedName("w_name_3")
    @ColumnInfo(name = "w_name_3")
    private String wordName3;

    @SerializedName("w_name_4")
    @ColumnInfo(name = "w_name_4")
    private String wordName4;

    @SerializedName("w_img_1")
    @ColumnInfo(name = "w_img_1")
    private String wordImg1;

    @SerializedName("w_img_2")
    @ColumnInfo(name = "w_img_2")
    private String wordImg2;

    @SerializedName("w_img_3")
    @ColumnInfo(name = "w_img_3")
    private String wordImg3;

    @SerializedName("w_img_4")
    @ColumnInfo(name = "w_img_4")
    private String wordImg4;

    @SerializedName("w_audio_1")
    @ColumnInfo(name = "w_audio_1")
    private String wordAudio1;

    @SerializedName("w_audio_2")
    @ColumnInfo(name = "w_audio_2")
    private String wordAudio2;

    @SerializedName("w_audio_3")
    @ColumnInfo(name = "w_audio_3")
    private String wordAudio3;

    @SerializedName("w_audio_4")
    @ColumnInfo(name = "w_audio_4")
    private String wordAudio4;

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getWordName1() {
        return wordName1;
    }

    public void setWordName1(String wordName1) {
        this.wordName1 = wordName1;
    }

    public String getWordName2() {
        return wordName2;
    }

    public void setWordName2(String wordName2) {
        this.wordName2 = wordName2;
    }

    public String getWordName3() {
        return wordName3;
    }

    public void setWordName3(String wordName3) {
        this.wordName3 = wordName3;
    }

    public String getWordName4() {
        return wordName4;
    }

    public void setWordName4(String wordName4) {
        this.wordName4 = wordName4;
    }

    public String getWordImg1() {
        return wordImg1;
    }

    public void setWordImg1(String wordImg1) {
        this.wordImg1 = wordImg1;
    }

    public String getWordImg2() {
        return wordImg2;
    }

    public void setWordImg2(String wordImg2) {
        this.wordImg2 = wordImg2;
    }

    public String getWordImg3() {
        return wordImg3;
    }

    public void setWordImg3(String wordImg3) {
        this.wordImg3 = wordImg3;
    }

    public String getWordImg4() {
        return wordImg4;
    }

    public void setWordImg4(String wordImg4) {
        this.wordImg4 = wordImg4;
    }

    public String getWordAudio1() {
        return wordAudio1;
    }

    public void setWordAudio1(String wordAudio1) {
        this.wordAudio1 = wordAudio1;
    }

    public String getWordAudio2() {
        return wordAudio2;
    }

    public void setWordAudio2(String wordAudio2) {
        this.wordAudio2 = wordAudio2;
    }

    public String getWordAudio3() {
        return wordAudio3;
    }

    public void setWordAudio3(String wordAudio3) {
        this.wordAudio3 = wordAudio3;
    }

    public String getWordAudio4() {
        return wordAudio4;
    }

    public void setWordAudio4(String wordAudio4) {
        this.wordAudio4 = wordAudio4;
    }
}
