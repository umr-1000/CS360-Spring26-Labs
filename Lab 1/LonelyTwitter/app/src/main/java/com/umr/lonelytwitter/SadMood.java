package com.umr.lonelytwitter;

import java.util.Date;

public class SadMood extends Mood {

    @Override
    public String currentMood() {
        return "Sad";
    }

    public SadMood() {
        super();
    }

    public SadMood(Date date) {
        super(date);
    }
}
