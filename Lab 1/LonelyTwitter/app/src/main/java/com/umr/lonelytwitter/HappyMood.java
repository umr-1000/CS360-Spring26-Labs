package com.umr.lonelytwitter;

import java.util.Date;

public class HappyMood extends Mood {

    @Override
    public String currentMood() {
        return "Happy";
    }

    public HappyMood() {
        super();
    }

    public HappyMood(Date date) {
        super(date);
    }
}
