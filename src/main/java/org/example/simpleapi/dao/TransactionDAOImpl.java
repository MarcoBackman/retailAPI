package org.example.simpleapi.dao;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.example.simpleapi.domain.Transaction;
import org.example.simpleapi.service.Month;
import org.example.simpleapi.mapper.TransactionRowMapper;
import org.example.simpleapi.util.DateUtil;
import org.example.simpleapi.util.IDateTime;
import org.example.simpleapi.util.TestOnly;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

//Todo: Migrate this into ORM tool(Hibernate, Jooq) for type safe programming

/**
 * This class handles transaction data fetch from H2 database - table TRANSACTION
 */
@Repository
@Log4j2
public class TransactionDAOImpl extends AbstractCommonDAO<Transaction> implements RecordDAO<Transaction>, IDateTime {

    TransactionRowMapper transactionRowMapper;

    public TransactionDAOImpl(JdbcTemplate jdbcTemplate,
                              TransactionRowMapper transactionRowMapper) {
        super(jdbcTemplate, transactionRowMapper);
        this.transactionRowMapper = transactionRowMapper;
    }

    @Override
    public Transaction getRecordById(Marker mk, int id) {
        String query = "SELECT * FROM TRANSACTION WHERE TRANSACTION_ID=?";
        Transaction result;
        try (PreparedStatement selectTransactionPrepStatement = getPreparedStatement(query)) {
            selectTransactionPrepStatement.setInt(1, id);
            result = transactionRowMapper.mapNextRow(selectTransactionPrepStatement.executeQuery());
        } catch (SQLException exception) {
            log.warn(mk, "SQL error. Could not fetch transaction information", exception);
            return null;
        }
        if (result == null) {
            log.info(mk, "No transaction data fetched for customerId={}", id);
            return null;
        }
        log.debug(mk, "Transaction result={}", result);
        return result;
    }

    public List<Transaction> getRecordByCustomerId(Marker mk, int customerId) {
        String query = "SELECT * FROM TRANSACTION WHERE CUSTOMER_ID=?";
        List<Transaction> transacitons;
        try (PreparedStatement selectTransactionPrepStatement = getPreparedStatement(query)) {
            selectTransactionPrepStatement.setInt(1, customerId);
            ResultSet resultSet = selectTransactionPrepStatement.executeQuery();
            transacitons = transactionRowMapper.mapMultiRowList(resultSet);
        } catch (SQLException exception) {
            log.warn(mk, "SQL error. Could not fetch transaction information", exception);
            return null;
        }
        log.debug(mk, "DB transaction result={}", transacitons);
        return transacitons;
    }

    /**
     * @return returns -1 if SQL failed to execute, otherwise returns the generated key
     */
    @TestOnly
    @Override
    public long addRecord(Marker mk, Transaction transaction) {
        String query = "INSERT INTO TRANSACTION(TRAN_AMOUNT, TRAN_WHEN, CUSTOMER_ID)" +
                " values (?, ?, ?)";
        long generatedKeyValue;
        try (PreparedStatement insertTransactionPrepStatement = getPreparedStatement(query)) {
            insertTransactionPrepStatement.setBigDecimal(1, transaction.getTranAmount());
            insertTransactionPrepStatement.setTimestamp(2, DateUtil.convertOffsetDateTimeToTimeStamp(getCurrentOffsetDateTime()));
            insertTransactionPrepStatement.setInt(3, transaction.getCustomerId());
            int affectedRows = insertTransactionPrepStatement.executeUpdate();
            if (affectedRows > 0 && insertTransactionPrepStatement.getGeneratedKeys().next()) {
                generatedKeyValue = insertTransactionPrepStatement.getGeneratedKeys().getLong("TRANSACTION_ID");
            } else {
                generatedKeyValue = -1;
            }
        } catch (SQLException exception) {
            log.warn(mk, "SQL error. Could not fetch transaction information", exception);
            return -1;
        }
        return generatedKeyValue;
    }

    @Override
    public int updateRecord(Marker mk, Transaction customer) {
        //Empty implementation: we do not modify transaction in any case.
        return 0;
    }

    //all transactions by a customer and order by date

    public Stream<Transaction> getLastMonthTransactionsByCustomerId(Marker mk,
                                                                    int customerId,
                                                                    LocalDate today,
                                                                    Month month) {
        LocalDate minOfCurrentMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        today = today.minusMonths(month.getValue());
        LocalDate minOfLastMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);

        String query = """
                SELECT * FROM TRANSACTION WHERE CUSTOMER_ID=? and (TRAN_WHEN < ? and TRAN_WHEN>=?) ORDER BY TRAN_WHEN ASC
                 """;
        List<Transaction> transacitons;
        try (PreparedStatement selectTransactionPrepStatement = getPreparedStatement(query)) {
            selectTransactionPrepStatement.setInt(1, customerId);
            selectTransactionPrepStatement.setTimestamp(2, DateUtil.convertOffsetDateTimeToTimeStamp(minOfCurrentMonth));
            selectTransactionPrepStatement.setTimestamp(3, DateUtil.convertOffsetDateTimeToTimeStamp(minOfLastMonth));
            ResultSet resultSet = selectTransactionPrepStatement.executeQuery();
            transacitons = transactionRowMapper.mapMultiRowList(resultSet);
        } catch (SQLException exception) {
            log.warn(mk, "SQL error. Could not fetch transaction information", exception);
            return null;
        }

        return transacitons.stream();
    }
}
