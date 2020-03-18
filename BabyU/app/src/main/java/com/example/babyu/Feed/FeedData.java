package com.example.babyu.Feed;

public class FeedData {


    private String time;
    private String ml;
    private String date;


    public FeedData() {
    }

    public FeedData(String time, String ml) {
        this.time = time;
        this.ml = ml;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMl() {
        return ml;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}