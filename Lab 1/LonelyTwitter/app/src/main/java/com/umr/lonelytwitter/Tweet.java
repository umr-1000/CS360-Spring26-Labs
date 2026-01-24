package com.umr.lonelytwitter;

import java.util.Date;

public abstract class Tweet implements Tweetable {
    private Date date;
    private String message;

    public abstract Boolean isImportant();

    public Tweet(String message) {
        this.message = message;
        this.date = new Date();
    }

    public Tweet(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

