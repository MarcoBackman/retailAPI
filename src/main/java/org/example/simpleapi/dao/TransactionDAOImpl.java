package org.example.simpleapi.dao;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.example.simpleapi.domain.Transaction;
import org.example.simpleapi.service.Month;
import org.example.simpleapi.mapper.TransactionRowMapper;
import org.example.simpleapi.util.IDateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

//Todo: Migrate this into ORM tool(Hibernate, Jooq) for type safe programming
@Repository
@Log4j2
public class TransactionDAOImpl extends AbstractCommonDAO<Transaction> implements RecordDAO<Transaction>, IDateTime {

    public TransactionDAOImpl(JdbcTemplate jdbcTemplate,
                              TransactionRowMapper transactionRowMapper) {
        super(jdbcTemplate, transactionRowMapper);
    }

    @Override
    public Transaction getRecordById(Marker mk, int id) {
        String query = "SELECT * FROM TRANSACTION WHERE TRANSACTION_ID=?";
        List<Transaction> transacitons = jdbcTemplate.query(query, rowMapper, id);
        log.debug(mk, "DB transaction result={}", transacitons);
        return transacitons.isEmpty() ? null : transacitons.get(0);
    }

    public List<Transaction> getRecordByCustomerId(Marker mk, int customerId) {
        String query = "SELECT * FROM TRANSACTION WHERE CUSTOMER_ID=?";
        List<Transaction> transacitons = jdbcTemplate.query(query, rowMapper, customerId);
        log.debug(mk, "DB transaction result={}", transacitons);
        return transacitons;
    }

    @Override
    public int addRecord(Marker mk, Transaction transaction) {
        String query = "INSERT INTO TRANSACTION(TRAN_AMOUNT, TRAN_WHEN, CUSTOMER_ID) values (?, ?, ?)";
        int result = jdbcTemplate.update(
                query,
                transaction.getTranAmount(),
                getCurrentOffsetDateTime(),
                transaction.getCustomerId());
        log.debug(mk, "affected rows={}", result);
        return result;
    }

    @Override
    public int updateRecord(Marker mk, Transaction customer) {
        //Empty implementation: we do not modify transaction in any case.
        return 0;
    }

    //all transactions by a customer and order by date
    public Stream<Transaction> getLastMonthTransactionsByCustomerId(int customerId,
                                                                    LocalDate today,
                                                                    Month month) {
        LocalDate minOfCurrentMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        today = today.minusMonths(month.getValue());
        LocalDate minOfLastMonth = LocalDate.of(today.getYear(), today.getMonth(), 1);
        String query = """
                SELECT * FROM TRANSACTION WHERE CUSTOMER_ID=? and (TRAN_WHEN < ? and TRAN_WHEN>=?) ORDER BY TRAN_WHEN ASC
                 """;
        return jdbcTemplate.queryForStream(query, rowMapper,
                customerId,
                minOfCurrentMonth,
                minOfLastMonth);
    }
}
