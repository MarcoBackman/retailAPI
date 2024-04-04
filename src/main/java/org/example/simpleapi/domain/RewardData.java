package org.example.simpleapi.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RewardData {
    private String customerId;
    private String userName;
    private String lastMonthReward;
    private String lastTwoMonthsReward;
    private String lastThreeMonthsReward;
}
