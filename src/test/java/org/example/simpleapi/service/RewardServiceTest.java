package org.example.simpleapi.service;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.simpleapi.dao.CustomerDAOImpl;
import org.example.simpleapi.dao.TransactionDAOImpl;
import org.example.simpleapi.domain.RewardData;
import org.example.simpleapi.domain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {
    @InjectMocks
    RewardService rewardServiceSpy;
    @Mock
    CustomerDAOImpl customerDAO;
    @Mock
    TransactionDAOImpl transactionDAO;

    Marker mk = MarkerManager.getMarker("REWARD_SERV_TEST");

    private final BigDecimal LOW_BOUND_DOLLAR_SPENT = BigDecimal.valueOf(50);
    private final BigDecimal HIGH_BOUND_DOLLAR_SPENT = BigDecimal.valueOf(100);

    private final int TEST_CUSTOMER_ID = 1234;

    @BeforeEach
    public void init() {
        rewardServiceSpy = spy(new RewardService(customerDAO, transactionDAO));
    }

    @Test
    public void rewardCalculation_nearZeroTest() {
        BigDecimal testTranAmount = BigDecimal.ZERO;
        BigDecimal actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.ZERO);

        testTranAmount = new BigDecimal("-1");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.ZERO);

        testTranAmount = new BigDecimal("0.99999999");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void rewardCalculation_nearLowBoundDollarTest() {
        BigDecimal testTranAmount = LOW_BOUND_DOLLAR_SPENT;
        BigDecimal actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.ZERO);

        testTranAmount = new BigDecimal("51");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.ONE);

        testTranAmount = new BigDecimal("49.99999999");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.ZERO);

        testTranAmount = new BigDecimal("89.99999999");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(39));
    }

    @Test
    public void rewardCalculation_nearHighBoundDollarTest() {
        BigDecimal testTranAmount = HIGH_BOUND_DOLLAR_SPENT;
        BigDecimal actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(50));

        testTranAmount = new BigDecimal("99.99999999");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(49));

        testTranAmount = new BigDecimal("100.99999999");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(50));

        testTranAmount = new BigDecimal("101.99999999");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(52));
    }

    @Test
    public void rewardCalculation_beyondHighBoundDollarTest() {
        BigDecimal testTranAmount =  new BigDecimal("199.99999999");
        BigDecimal actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(248));

        testTranAmount = new BigDecimal("1299.99999999");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(2448));

        testTranAmount = new BigDecimal("100000.000001");
        actualResult = rewardServiceSpy.calculatePoints(mk, testTranAmount);
        assertThat(actualResult).isEqualByComparingTo(BigDecimal.valueOf(199850));
    }

    @Test
    public void rewardService_getter_allThreeMonths_executionTest() {
        Stream<Transaction> lastMonthTransactionStream
                = Stream.of(Transaction.builder().tranAmount(new BigDecimal("123.523")).build());

        Stream<Transaction> lastSecondMonthTransactionStream
                = Stream.of(Transaction.builder().tranAmount(new BigDecimal("523.523")).build(), //896
                            Transaction.builder().tranAmount(new BigDecimal("63.523")).build()); //13

        Stream<Transaction> lastThirdMonthTransactionStream
                = Stream.of(Transaction.builder().tranAmount(new BigDecimal("23.523")).build(),
                            Transaction.builder().tranAmount(new BigDecimal("663.523")).build());

        RewardData rewardDataSpy = spy(new RewardData(TEST_CUSTOMER_ID));

        doReturn(lastMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_MONTH));
        doReturn(lastSecondMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_SECOND_MONTH));
        doReturn(lastThirdMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_THIRD_MONTH));

        rewardServiceSpy.getCustomerRewards(mk, TEST_CUSTOMER_ID, rewardDataSpy);

        verify(rewardDataSpy, times(1)).addPreviousMonthReward(BigDecimal.valueOf(96));
        verify(rewardDataSpy, times(1)).addLastSecondMonthReward(BigDecimal.valueOf(896));
        verify(rewardDataSpy, times(1)).addLastSecondMonthReward(BigDecimal.valueOf(13));
        verify(rewardDataSpy, times(1)).addLastThirdMonthReward(BigDecimal.ZERO);
        verify(rewardDataSpy, times(1)).addLastThirdMonthReward(BigDecimal.valueOf(1176));
        verify(rewardDataSpy, times(1)).setLastThreeMonthsTotalRewards();

        assertThat(rewardDataSpy.getTotalThreeMonthsRewards()).isEqualByComparingTo(BigDecimal.valueOf(2181));
    }

    @Test
    public void rewardService_getter_noTransaction_onLastSecondMonth_executionTest() {
        Stream<Transaction> lastMonthTransactionStream
                = Stream.of(Transaction.builder().tranAmount(new BigDecimal("123.523")).build());

        Stream<Transaction> lastSecondMonthTransactionStream
                = Stream.empty();

        Stream<Transaction> lastThirdMonthTransactionStream
                = Stream.of(Transaction.builder().tranAmount(new BigDecimal("23.523")).build(),
                Transaction.builder().tranAmount(new BigDecimal("663.523")).build());

        RewardData rewardDataSpy = spy(new RewardData(TEST_CUSTOMER_ID));

        doReturn(lastMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_MONTH));
        doReturn(lastSecondMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_SECOND_MONTH));
        doReturn(lastThirdMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_THIRD_MONTH));

        rewardServiceSpy.getCustomerRewards(mk, TEST_CUSTOMER_ID, rewardDataSpy);

        verify(rewardDataSpy, times(1)).addPreviousMonthReward(BigDecimal.valueOf(96));
        verify(rewardDataSpy, times(0)).addLastSecondMonthReward(any());
        verify(rewardDataSpy, times(1)).addLastThirdMonthReward(BigDecimal.ZERO);
        verify(rewardDataSpy, times(1)).addLastThirdMonthReward(BigDecimal.valueOf(1176));
        verify(rewardDataSpy, times(1)).setLastThreeMonthsTotalRewards();

        assertThat(rewardDataSpy.getTotalThreeMonthsRewards()).isEqualByComparingTo(BigDecimal.valueOf(1272));
    }

    @Test
    public void rewardService_getter_noTransactionAtAll_executionTest() {
        Stream<Transaction> lastMonthTransactionStream
                = Stream.empty();

        Stream<Transaction> lastSecondMonthTransactionStream
                = Stream.empty();

        Stream<Transaction> lastThirdMonthTransactionStream
                = Stream.empty();

        RewardData rewardDataSpy = spy(new RewardData(TEST_CUSTOMER_ID));

        doReturn(lastMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_MONTH));
        doReturn(lastSecondMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_SECOND_MONTH));
        doReturn(lastThirdMonthTransactionStream).when(transactionDAO)
                .getLastMonthTransactionsByCustomerId(eq(mk), eq(TEST_CUSTOMER_ID), any(), eq(Month.LAST_THIRD_MONTH));

        rewardServiceSpy.getCustomerRewards(mk, TEST_CUSTOMER_ID, rewardDataSpy);

        verify(rewardDataSpy, times(0)).addPreviousMonthReward(any());
        verify(rewardDataSpy, times(0)).addLastSecondMonthReward(any());
        verify(rewardDataSpy, times(0)).addLastThirdMonthReward(any());

        assertThat(rewardDataSpy.getTotalThreeMonthsRewards()).isEqualByComparingTo(BigDecimal.valueOf(0));
    }
}
