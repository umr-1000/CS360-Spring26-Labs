package com.umr.lonelytwitter;

import java.util.Date;

public class NormalTweet extends Tweet {
    @Override
    public Boolean isImportant() {
        return false;
    }

    public NormalTweet(String message) {
        super(message);
    }

    public NormalTweet(String message, Date date) {
        super(message, date);
    }
}
