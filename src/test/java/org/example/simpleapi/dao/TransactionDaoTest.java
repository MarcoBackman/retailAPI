package org.example.simpleapi.dao;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.example.simpleapi.config.H2TestConfig;
import org.example.simpleapi.domain.Transaction;
import org.example.simpleapi.service.Month;
import org.example.simpleapi.mapper.TransactionRowMapper;
import org.example.simpleapi.util.DateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@ActiveProfiles(profiles = "test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {TransactionDAOImpl.class,H2TestConfig.class,TransactionRowMapper.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class TransactionDaoTest {

    @Autowired
    @Qualifier("jdbcTestTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionDAOImpl transactionDAO;

    Marker mk;

    private final String todayTestDateStr = "2024-04-05 09:00:00 -04:00";
    private final String currentMonthTestDateStr = "2024-04-05 09:00:00 -04:00";
    private final String previousMonthTestDateStr = "2024-03-05 09:00:00 -04:00";
    private final String twoMonthsBeforeTestDateStr = "2024-02-05 09:00:00 -04:00";
    private final String threeMonthsBeforeTestDateStr = "2024-01-05 09:00:00 -04:00";
    private final String oldMonthBeforeTestDateStr = "2023-01-05 09:00:00 -04:00";

    private final LocalDate today = LocalDate.of(2024, 4, 5);


    private final int FIRST_TEST_CUSTOMER_ID = 1234;
    private final int SECOND_TEST_CUSTOMER_ID = 1235;

    @BeforeEach
    public void init() {
        mk = MarkerManager.getMarker("TRA_DAO_TEST");
        transactionDAO = Mockito.spy(transactionDAO);
        String query = """
                 CREATE TABLE CUSTOMER (CUSTOMER_ID bigint AUTO_INCREMENT PRIMARY KEY, USER_NAME VARCHAR2(40) UNIQUE, FIRST_NAME VARCHAR2(40), LAST_NAME VARCHAR2(40));
                 CREATE TABLE TRANSACTION (TRANSACTION_ID bigint AUTO_INCREMENT PRIMARY KEY, CUSTOMER_ID bigint ,TRAN_WHEN TIMESTAMP WITH TIME ZONE, TRAN_AMOUNT NUMERIC(20, 3));
                """;
        jdbcTemplate.execute(query);

        OffsetDateTime fixedDateTime = DateUtil.getOffsetDateTimeFromString(todayTestDateStr);
        lenient().doReturn(fixedDateTime).when(transactionDAO).getCurrentOffsetDateTime();
    }

    @AfterEach
    public void after() {
        String query = """
                 DROP TABLE CUSTOMER;
                 DROP TABLE TRANSACTION;
                """;
        jdbcTemplate.execute(query);
    }

    @Test
    public void transactionAdditionTest() {
        Transaction stubTrans = Transaction.builder()
                .tranAmount(new BigDecimal("54.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build();
        int result = transactionDAO.addRecord(mk, stubTrans);
        assertThat(result).isEqualByComparingTo(1);

        Transaction transaction = transactionDAO.getRecordByCustomerId(mk, 1234).get(0);
        assertThat(transaction.getTransactionId()).isNotNull();
        assertThat(transaction.getTranAmount()).isEqualByComparingTo(new BigDecimal("54.234"));
        assertThat(transaction.getTranWhen()).isEqualTo("2024-04-05T09:00-04:00");
        assertThat(transaction.getCustomerId()).isEqualTo(FIRST_TEST_CUSTOMER_ID);
    }

    @Test
    public void two_transactionsAdditionTest() {
        Transaction firstStubTrans = Transaction.builder()
                .tranAmount(new BigDecimal("54.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build();

        Transaction secondStubTrans = Transaction.builder()
                .tranAmount(new BigDecimal("54.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build();
        int result = transactionDAO.addRecord(mk, firstStubTrans);
        assertThat(result).isEqualByComparingTo(1);
        result = transactionDAO.addRecord(mk, secondStubTrans);
        assertThat(result).isEqualByComparingTo(1);

        List<Transaction> transaction = transactionDAO.getRecordByCustomerId(mk, FIRST_TEST_CUSTOMER_ID);

        assertThat(transaction.size()).isEqualTo(2);
    }

    private void addCurrentMonthTransactions() {
        OffsetDateTime thisMonthDate = DateUtil.getOffsetDateTimeFromString(currentMonthTestDateStr);
        doReturn(thisMonthDate).when(transactionDAO).getCurrentOffsetDateTime();

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("54.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build());

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("104.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build());
    }

    private void addLastMonthTransactions() {
        OffsetDateTime lastMonthDateTime = DateUtil.getOffsetDateTimeFromString(previousMonthTestDateStr);
        doReturn(lastMonthDateTime).when(transactionDAO).getCurrentOffsetDateTime();

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("254.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build());

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("24.234"))
                .customerId(SECOND_TEST_CUSTOMER_ID)
                .build());
    }

    private void addTwoMonthsBeforeTransactions() {
        OffsetDateTime lastSecondMothDateTime = DateUtil.getOffsetDateTimeFromString(twoMonthsBeforeTestDateStr);
        doReturn(lastSecondMothDateTime).when(transactionDAO).getCurrentOffsetDateTime();

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("275.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build());

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("2324.234"))
                .customerId(SECOND_TEST_CUSTOMER_ID)
                .build());
    }

    private void addThreeMonthsBeforeTransactions() {
        OffsetDateTime lastThirdMothDateTime = DateUtil.getOffsetDateTimeFromString(threeMonthsBeforeTestDateStr);
        doReturn(lastThirdMothDateTime).when(transactionDAO).getCurrentOffsetDateTime();

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("654.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build());

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("4.234"))
                .customerId(SECOND_TEST_CUSTOMER_ID)
                .build());
    }

    private void addOutdatedTransactions() {
        OffsetDateTime oldDateTime = DateUtil.getOffsetDateTimeFromString(oldMonthBeforeTestDateStr);
        doReturn(oldDateTime).when(transactionDAO).getCurrentOffsetDateTime();

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("10054.234"))
                .customerId(FIRST_TEST_CUSTOMER_ID)
                .build());

        transactionDAO.addRecord(mk, Transaction.builder()
                .tranAmount(new BigDecimal("2423.234"))
                .customerId(SECOND_TEST_CUSTOMER_ID)
                .build());
    }

    @Test
    public void getTransactionsByDateAndCustomerId_onlyLastMonthTest() {
        addCurrentMonthTransactions();
        addLastMonthTransactions();

        List<Transaction> totalTransactions = transactionDAO.getRecordByCustomerId(mk, FIRST_TEST_CUSTOMER_ID);
        assertThat(totalTransactions.size()).isEqualTo(3);

        List<Transaction> transactions
                = transactionDAO.getLastMonthTransactionsByCustomerId(FIRST_TEST_CUSTOMER_ID, today, Month.LAST_MONTH).toList();

        assertThat(transactions.size()).isEqualTo(1);
    }

    @Test
    public void getTransactionsByDateAndCustomerId_fromLastSecondMonthTest() {
        addCurrentMonthTransactions();
        addLastMonthTransactions();
        addTwoMonthsBeforeTransactions();

        List<Transaction> totalTransactions = transactionDAO.getRecordByCustomerId(mk, FIRST_TEST_CUSTOMER_ID);
        assertThat(totalTransactions.size()).isEqualTo(4);

        List<Transaction> transactions
                = transactionDAO.getLastMonthTransactionsByCustomerId(FIRST_TEST_CUSTOMER_ID, today, Month.LAST_SECOND_MONTH).toList();

        assertThat(transactions.size()).isEqualTo(2);
    }

    @Test
    public void getTransactionsByDateAndCustomerId_fromLastThirdMonthTest() {
        addCurrentMonthTransactions();
        addLastMonthTransactions();
        addTwoMonthsBeforeTransactions();
        addThreeMonthsBeforeTransactions();

        List<Transaction> totalTransactions = transactionDAO.getRecordByCustomerId(mk, FIRST_TEST_CUSTOMER_ID);
        assertThat(totalTransactions.size()).isEqualTo(5);

        List<Transaction> transactions
                = transactionDAO.getLastMonthTransactionsByCustomerId(FIRST_TEST_CUSTOMER_ID, today, Month.LAST_THIRD_MONTH).toList();

        assertThat(transactions.size()).isEqualTo(3);
    }
}
