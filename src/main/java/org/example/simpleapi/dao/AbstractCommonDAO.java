package org.example.simpleapi.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Log4j2
public abstract class AbstractCommonDAO<T> {

    protected RowMapper<T> rowMapper;
    protected JdbcTemplate jdbcTemplate;

    protected AbstractCommonDAO(JdbcTemplate jdbcTemplate,
                         RowMapper<T> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    //We can use namedParameterJdbcTemplate alternatively
    protected PreparedStatement getPreparedStatement(String query) throws SQLException {
        return Objects.requireNonNull(jdbcTemplate.getDataSource())
                .getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

}
