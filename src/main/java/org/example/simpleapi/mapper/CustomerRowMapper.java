package org.example.simpleapi.mapper;

import org.example.simpleapi.domain.Customer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper to assist DB data result to POJO Customer object
 */
@Component
public class CustomerRowMapper implements RowMapper<Customer>, Serializable {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Customer.builder()
                .customerId((int)rs.getLong("CUSTOMER_ID"))
                .userName(rs.getString("USER_NAME"))
                .firstName(rs.getString("FIRST_NAME"))
                .lastName(rs.getString("LAST_NAME"))
                .build();
    }

    //This method uses rs.next for primary key-based search
    public Customer mapNextRow(ResultSet rs) throws SQLException {
        rs.next();
        return Customer.builder()
                .customerId((int)rs.getLong("CUSTOMER_ID"))
                .userName(rs.getString("USER_NAME"))
                .firstName(rs.getString("FIRST_NAME"))
                .lastName(rs.getString("LAST_NAME"))
                .build();
    }
}
