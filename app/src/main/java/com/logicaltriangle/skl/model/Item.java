package com.logicaltriangle.skl.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Item {

    @SerializedName("item_id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    private int itemId;

    @SerializedName("cat_id")
    @ColumnInfo(name = "cat_id")
    private int catId;

    @SerializedName("item_name")
    @ColumnInfo(name = "item_name")
    private String itemName;

    @SerializedName("item_img_url")
    @ColumnInfo(name = "item_img_url")
    private String itemImgUrl;

    @SerializedName("item_audio_url")
    @ColumnInfo(name = "item_audio_url")
    private String itemAudioUrl;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getItemAudioUrl() {
        return itemAudioUrl;
    }

    public void setItemAudioUrl(String itemAudioUrl) {
        this.itemAudioUrl = itemAudioUrl;
    }
}
