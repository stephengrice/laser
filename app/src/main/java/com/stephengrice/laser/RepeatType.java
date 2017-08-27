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

    public static RepeatType fromInt(int i) {
        switch(i) {
            case 1:
                return NO_REPEAT;
            case 2:
                return DAILY;
            case 3:
                return WEEKLY;
            case 4:
                return BI_WEEKLY;
            case 5:
                return MONTHLY;
            case 6:
                return YEARLY;
            default:
                return null;
        }
    }
}
