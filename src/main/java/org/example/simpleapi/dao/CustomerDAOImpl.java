package org.example.simpleapi.dao;

import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.example.simpleapi.domain.Customer;
import org.example.simpleapi.mapper.CustomerRowMapper;
import org.example.simpleapi.util.TestOnly;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

//Todo: Migrate this into ORM tool(Hibernate, Jooq) for type safe programming

/**
 * This class handles customer data fetch from H2 database - table CUSTOMER
 */
@Repository
@Log4j2
public class CustomerDAOImpl extends AbstractCommonDAO<Customer> implements RecordDAO<Customer> {

    CustomerRowMapper customerRowMapper;

    public CustomerDAOImpl(JdbcTemplate jdbcTemplate,
                           CustomerRowMapper customerRowMapper) {
        super(jdbcTemplate, customerRowMapper);
        this.customerRowMapper = customerRowMapper;
    }

    //Get Customer record by customerId
    @Nullable
    @Override
    public Customer getRecordById(Marker mk, int id) {
        String query = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID=?";
        Customer result;
        try (PreparedStatement selectCustomerPrepStatement = getPreparedStatement(query)) {
            selectCustomerPrepStatement.setInt(1, id);
            result = customerRowMapper.mapNextRow(selectCustomerPrepStatement.executeQuery());
        } catch (SQLException exception) {
            log.warn(mk, "SQL error. Could not fetch customer information", exception);
            return null;
        }

        if (result == null) {
            log.info(mk, "no customer data fetched for customerId={}", id);
            return null;
        }
        log.debug(mk, "customerResult={}", result);
        return result;
    }

    /**
     * @return returns -1 if SQL failed to execute, otherwise returns the generated key
     */
    @TestOnly
    @Override
    public long addRecord(Marker mk, Customer customer) {
        String query = "INSERT INTO CUSTOMER(CUSTOMER_ID, USER_NAME, FIRST_NAME, LAST_NAME)" +
                " values (?, ?, ?, ?)";
        long generatedKeyValue;
        try (PreparedStatement insertCustomerPrepStatement = getPreparedStatement(query)) {
            insertCustomerPrepStatement.setLong(1, customer.getCustomerId());
            insertCustomerPrepStatement.setString(2, customer.getUserName());
            insertCustomerPrepStatement.setString(3, customer.getFirstName());
            insertCustomerPrepStatement.setString(4, customer.getLastName());
            int affectedRows = insertCustomerPrepStatement.executeUpdate();
            if (affectedRows > 0 && insertCustomerPrepStatement.getGeneratedKeys().next()) {
                generatedKeyValue = insertCustomerPrepStatement.getGeneratedKeys().getLong("CUSTOMER_ID");
            } else {
                generatedKeyValue = -1;
            }
        } catch (SQLException exception) {
            log.warn(mk, "SQL error. Could not fetch customer information", exception);
            return -1;
        }
        return generatedKeyValue;
    }

    @Override
    public int updateRecord(Marker mk, Customer customer) {
        String query = "UPDATE CUSTOMER SET FIRST_NAME=?, LAST_NAME=? WHERE CUSTOMER_ID=?";
        int result;
        try (PreparedStatement insertCustomerPrepStatement = getPreparedStatement(query)) {
            insertCustomerPrepStatement.setString(1, customer.getFirstName());
            insertCustomerPrepStatement.setString(2, customer.getLastName());
            insertCustomerPrepStatement.setInt(3, customer.getCustomerId());
            result = insertCustomerPrepStatement.executeUpdate();
        } catch (SQLException exception) {
            log.warn(mk, "SQL error. Could not fetch customer information", exception);
            return 0;
        }
        log.debug(mk, "affected rows={}", result);
        return result;
    }
}
