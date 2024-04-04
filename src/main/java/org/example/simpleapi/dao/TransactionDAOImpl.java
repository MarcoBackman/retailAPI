package org.example.simpleapi.dao;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.example.simpleapi.domain.Transaction;
import org.example.simpleapi.mapper.TransactionRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

//Todo: Migrate this into ORM tool(Hibernate, Jooq) for type safe programming
@Repository
@Log4j2
public class TransactionDAOImpl extends AbstractCommonDAO<Transaction> implements RecordDAO<Transaction> {

    public TransactionDAOImpl(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              TransactionRowMapper transactionRowMapper) {
        super(jdbcTemplate, namedParameterJdbcTemplate, transactionRowMapper);
    }

    @Override
    public Transaction getRecordById(Marker mk, int id) {
        String query = "SELECT * FROM TRANSACTION WHERE TRANSACTION_ID=?";
        List<Transaction> customerResult = jdbcTemplate.query(query, rowMapper, id);
        log.debug(mk, "customerResult={}", customerResult);
        return customerResult.isEmpty() ? null : customerResult.get(0);
    }

    @Override
    public int addRecord(Marker mk, Transaction transaction) {
        String query = "INSERT INTO TRANSACTION(TRAN_AMOUNT, TRAN_WHEN) values (?, ?)";
        int result = jdbcTemplate.update(query, rowMapper,
                transaction.getTranAmount(),
                OffsetDateTime.now());
        log.debug(mk, "affected rows={}", result);
        return result;
    }

    @Override
    public int updateRecord(Marker mk, Transaction customer) {
        //Empty implementation: we do not modify transaction in any case.
        return 0;
    }
}
