package com.example.babyu.Diary;

import com.google.firebase.database.Exclude;

public class Upload {
    private String date,remarks;
    private String imageUrl;
    private String key;

    public Upload(){

    }

    public Upload(String date, String remarks, String imageUrl) {
        this.date = date;
        this.remarks = remarks;
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
