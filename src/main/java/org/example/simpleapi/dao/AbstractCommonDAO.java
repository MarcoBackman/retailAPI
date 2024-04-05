package org.example.simpleapi.dao;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;

@Log4j2
public abstract class AbstractCommonDAO<T> {

    protected RowMapper<T> rowMapper;
    protected JdbcTemplate jdbcTemplate;

    protected AbstractCommonDAO(JdbcTemplate jdbcTemplate,
                         RowMapper<T> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

}
