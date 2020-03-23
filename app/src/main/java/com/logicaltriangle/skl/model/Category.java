package com.logicaltriangle.skl.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Category {

    @SerializedName("cat_id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cat_id")
    private int catId;

    @SerializedName("cat_name")
    @ColumnInfo(name = "cat_name")
    private String catName;

    @SerializedName("cat_image_url")
    @ColumnInfo(name = "cat_image_url")
    private String catImgUrl;

    @SerializedName("cat_audio_url")
    @ColumnInfo(name = "cat_audio_url")
    private String catAudioUrl;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImgUrl() {
        return catImgUrl;
    }

    public void setCatImgUrl(String catImgUrl) {
        this.catImgUrl = catImgUrl;
    }

    public String getCatAudioUrl() {
        return catAudioUrl;
    }

    public void setCatAudioUrl(String catAudioUrl) {
        this.catAudioUrl = catAudioUrl;
    }
}
