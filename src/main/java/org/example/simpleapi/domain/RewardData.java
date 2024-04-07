package org.example.simpleapi.domain;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

/**
 * RewardData class object stores all crucial reward information
 */
@Getter
@Log4j2
public class RewardData {
    private final int customerId;
    private BigDecimal previousMonthReward = BigDecimal.ZERO;
    private BigDecimal lastSecondMonthReward = BigDecimal.ZERO;
    private BigDecimal lastThirdMonthReward = BigDecimal.ZERO;
    private BigDecimal totalThreeMonthsRewards = BigDecimal.ZERO;

    public RewardData(int customerId) {
        this.customerId = customerId;
    }

    public void addPreviousMonthReward(BigDecimal reward) {
        previousMonthReward = previousMonthReward.add(reward);
    }

    public void addLastSecondMonthReward(BigDecimal reward) {
        lastSecondMonthReward = lastSecondMonthReward.add(reward);
    }

    public void addLastThirdMonthReward(BigDecimal reward) {
        lastThirdMonthReward = lastThirdMonthReward.add(reward);
    }

    public void setLastThreeMonthsTotalRewards() {
        log.debug("Added all three months points. previousMonthReward={}," +
                        " lastSecondMonthReward={}, lastThirdMonthReward={}",
                previousMonthReward, lastSecondMonthReward, lastThirdMonthReward);
        totalThreeMonthsRewards = totalThreeMonthsRewards.add(previousMonthReward);
        totalThreeMonthsRewards = totalThreeMonthsRewards.add(lastSecondMonthReward);
        totalThreeMonthsRewards = totalThreeMonthsRewards.add(lastThirdMonthReward);
    }
}
