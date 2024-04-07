package org.example.simpleapi.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Getter
@Configuration
@Log4j2
public class JdbcConfig {
    @Value("${spring.datasource.driverClassName}")
    private String jdbcDriver;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUserName;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    public JdbcConfig() {
        log.info("Default config loaded. class={}", this.getClass().getName());
    }

    @Bean
    public DataSource jdbcDataSource() {
        log.info("Default property loaded. [jdbcDriver={}, jdbcUrl={}, jdbcUser={}, jdbcPass={}]",
                getJdbcDriver(), getJdbcUrl(), getJdbcUserName(), getJdbcPassword().substring(0, 2) + "XXXX");
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(getJdbcDriver());
        driverManagerDataSource.setUrl(getJdbcUrl());
        driverManagerDataSource.setUsername(getJdbcUserName());
        driverManagerDataSource.setPassword(getJdbcPassword());
        return driverManagerDataSource;
    }

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(jdbcDataSource());
    }

    /**
     * Alternative querying tool for PreparedStatement
     * @return
     */
    @Bean
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(jdbcDataSource());
    }

}