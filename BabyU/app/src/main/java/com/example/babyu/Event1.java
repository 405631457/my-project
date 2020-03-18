package com.example.babyu;

public class Event1 {
    long id;
    long epochTime;
    String date;
    String  desc;

    public Event1 (long id, long epochTime, String date, String  desc){
        this.id = id;
        this.epochTime = epochTime;
        this.date = date;
        this.desc = desc;
    }
}
