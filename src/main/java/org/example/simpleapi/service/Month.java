package org.example.simpleapi.service;

/**
 * Month enum for reward retrieve range by each month. <br/>
 * For example, lets say today is April/4/2024. <br/>
 * LAST_MONTH - for previous month: March/1/2024 ~ March/31/2024 <br/>
 * LAST_SECOND_MONTH - for last second months: February/1/2024 ~ February/29/2024 <br/>
 * LAST_THIRD_MONTH - for last second months: January/1/2024 ~ February/31/2024 <br/>
 */
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
