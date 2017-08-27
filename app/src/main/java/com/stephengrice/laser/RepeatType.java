package com.stephengrice.laser;

/**
 * Created by steph on 8/24/2017.
 */

public enum RepeatType {
    NO_REPEAT(0),
    DAILY(1),
    WEEKLY(2),
    BI_WEEKLY(3),
    MONTHLY(4),
    YEARLY(5);

    private int value;

    RepeatType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RepeatType fromInt(int i) {
        switch(i) {
            case 0:
                return NO_REPEAT;
            case 1:
                return DAILY;
            case 2:
                return WEEKLY;
            case 3:
                return BI_WEEKLY;
            case 4:
                return MONTHLY;
            case 5:
                return YEARLY;
            default:
                return null;
        }
    }
}
