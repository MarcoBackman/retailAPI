package org.example.simpleapi.mapper;

import org.example.simpleapi.domain.Transaction;
import org.example.simpleapi.util.DateUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TransactionRowMapper implements RowMapper<Transaction>, Serializable {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Transaction.builder()
                .customerId(rs.getInt("CUSTOMER_ID"))
                .transactionId(rs.getInt("TRANSACTION_ID"))
                .tranAmount(rs.getBigDecimal("TRAN_AMOUNT"))
                .tranWhen(DateUtil.getOffsetDateTimeFromString(rs.getString("TRAN_WHEN")))
                .build();
    }
}
