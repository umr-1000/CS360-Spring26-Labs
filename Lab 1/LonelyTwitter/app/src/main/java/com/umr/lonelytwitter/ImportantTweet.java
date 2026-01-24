package com.umr.lonelytwitter;

import java.util.Date;

public class ImportantTweet extends Tweet {
    @Override
    public Boolean isImportant() {
        return true;
    }

    public ImportantTweet(String message) {
        super(message);
    }

    public ImportantTweet(String message, Date date) {
        super(message, date);
    }
}
