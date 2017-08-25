package com.stephengrice.laser;

/**
 * Created by steph on 8/24/2017.
 */

public enum RepeatType {
    NO_REPEAT(1),
    DAILY(2),
    WEEKLY(3),
    BI_WEEKLY(4),
    MONTHLY(5),
    YEARLY(6);

    private int value;

    RepeatType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
