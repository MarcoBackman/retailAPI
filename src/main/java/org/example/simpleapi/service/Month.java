package org.example.simpleapi.service;

public enum Month {
    LAST_MONTH(1),
    LAST_SECOND_MONTH(2),
    LAST_THIRD_MONTH(3);

    private final int monthAmount;

    Month(int monthAmount) {
        this.monthAmount = monthAmount;
    }

    public int getValue() {
        return this.monthAmount;
    }
}
