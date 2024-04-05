package org.example.simpleapi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Getter
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
        totalThreeMonthsRewards = totalThreeMonthsRewards.add(previousMonthReward
                .add(lastSecondMonthReward)
                .add(lastThirdMonthReward));
    }
}
