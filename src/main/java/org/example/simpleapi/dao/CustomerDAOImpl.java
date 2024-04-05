package org.example.simpleapi.dao;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.example.simpleapi.domain.Customer;
import org.example.simpleapi.mapper.CustomerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

//Todo: Migrate this into ORM tool(Hibernate, Jooq) for type safe programming
@Repository
@Log4j2
public class CustomerDAOImpl extends AbstractCommonDAO<Customer> implements RecordDAO<Customer> {
    public CustomerDAOImpl(JdbcTemplate jdbcTemplate,
                           CustomerRowMapper customerRowMapper) {
        super(jdbcTemplate, customerRowMapper);
    }

    @Override
    public Customer getRecordById(Marker mk, int id) {
        String query = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID=?";
        List<Customer> customerResult = jdbcTemplate.query(query, rowMapper, id);
        log.debug(mk, "customerResult={}", customerResult);
        return customerResult.isEmpty() ? null : customerResult.get(0);
    }

    @Override
    public int addRecord(Marker mk, Customer customer) {
        String query = "INSERT INTO CUSTOMER(USER_NAME, FIRST_NAME, LAST_NAME) values (?, ?, ?)";
        int result = jdbcTemplate.update(query, rowMapper,
                customer.getUserName(),
                customer.getFirstName(),
                customer.getLastName());
        log.debug(mk, "affected rows={}", result);
        return result;
    }

    @Override
    public int updateRecord(Marker mk, Customer customer) {
        String query = "UPDATE CUSTOMER SET FIRST_NAME=?, LAST_NAME=? WHERE CUSTOMER_ID=?";
        int result = jdbcTemplate.update(query, rowMapper,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getCustomerId());
        log.debug(mk, "affected rows={}", result);
        return result;
    }

    public Stream<Integer> getAllCustomersId(Marker mk) {
        String query = "SELECT CUSTOMER_ID FROM CUSTOMER";
        return jdbcTemplate.queryForStream(query, (rs, rowNum) -> rs.getInt(0));
    }
}
