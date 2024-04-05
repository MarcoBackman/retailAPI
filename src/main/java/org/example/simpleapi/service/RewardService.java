package org.example.simpleapi.service;

import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.logging.log4j.Marker;
import org.example.simpleapi.dao.CustomerDAOImpl;
import org.example.simpleapi.dao.TransactionDAOImpl;
import org.example.simpleapi.domain.RewardData;

import org.example.simpleapi.domain.Transaction;
import org.example.simpleapi.util.IDateTime;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Log4j2
public class RewardService implements IDateTime {

    private final BigDecimal SINGLE_POINT_REWARD = BigDecimal.ONE;
    private final BigDecimal DOUBLE_POINT_REWARD = BigDecimal.valueOf(2);

    //50 dollars ~ 100 dollars will give 1 points for every dollar
    private final BigDecimal LOW_BOUND_DOLLAR_SPENT = BigDecimal.valueOf(50);

    //100 dollars will give 2 points for every dollar
    private final BigDecimal HIGH_BOUND_DOLLAR_SPENT = BigDecimal.valueOf(100);

    private final CustomerDAOImpl customerDAO;
    private final TransactionDAOImpl transactionDAO;

    RewardService(CustomerDAOImpl customerDAO,
                  TransactionDAOImpl transactionDAO) {
        this.customerDAO = customerDAO;
        this.transactionDAO = transactionDAO;
    }

    protected BigDecimal calculatePoints(Marker mk, BigDecimal tranAmount) {
        if (tranAmount.compareTo(LOW_BOUND_DOLLAR_SPENT) <= 0) {
            log.debug(mk, "Skippling point calculation result");
            return BigDecimal.valueOf(0);
        } if (tranAmount.compareTo(HIGH_BOUND_DOLLAR_SPENT) <= 0) {
            BigDecimal extraSpentDollars = tranAmount.subtract(LOW_BOUND_DOLLAR_SPENT)
                    .divide(BigDecimal.ONE, 0, RoundingMode.FLOOR);
            return extraSpentDollars.multiply(SINGLE_POINT_REWARD);
        } else {
            BigDecimal extraSpentDollars = tranAmount.subtract(HIGH_BOUND_DOLLAR_SPENT)
                    .divide(BigDecimal.ONE, 0, RoundingMode.FLOOR);
            //Initially will receive all low bound rewards - add 50 dollars
            return extraSpentDollars.multiply(DOUBLE_POINT_REWARD).add(LOW_BOUND_DOLLAR_SPENT);
        }
    }

    //Get a customers recent rewards (3 months)
    public RewardData getCustomerRewards(Marker mk, int customerId, RewardData rewardData) {
        log.info(mk, "Processing customer rewards for customerId={}", customerId);

        transactionDAO.getLastMonthTransactionsByCustomerId(customerId, getCurrentLocalDate(), Month.LAST_MONTH)
                .map(Transaction::getTranAmount)
                .forEach(tranAmount -> {
                    BigDecimal calculatedPoints = calculatePoints(mk, tranAmount);
                    rewardData.addPreviousMonthReward(calculatedPoints);
                });
        transactionDAO.getLastMonthTransactionsByCustomerId(customerId, getCurrentLocalDate(), Month.LAST_SECOND_MONTH)
                .map(Transaction::getTranAmount)
                .forEach(tranAmount -> {
                    BigDecimal calculatedPoints = calculatePoints(mk, tranAmount);
                    rewardData.addLastSecondMonthReward(calculatedPoints);
                });
        transactionDAO.getLastMonthTransactionsByCustomerId(customerId, getCurrentLocalDate(), Month.LAST_THIRD_MONTH)
                .map(Transaction::getTranAmount)
                .forEach(tranAmount -> {
                    BigDecimal calculatedPoints = calculatePoints(mk, tranAmount);
                    rewardData.addLastThirdMonthReward(calculatedPoints);
                });

        rewardData.setLastThreeMonthsTotalRewards();

        log.info(mk, "Processing Done.");

        return rewardData;
    }
}
