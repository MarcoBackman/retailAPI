package org.example.simpleapi.config;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Profile("test")
@Configuration
@Getter
@Log4j2
public class H2TestConfig {
    @Value("${spring.datasource.driverClassName}")
    private String jdbcDriver;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUserName;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    public H2TestConfig() {
        log.info("Test config loaded. class={}", this.getClass().getName());
    }

    @Bean
    @Profile("test")
    public DataSource jdbcDataSourceTest() {
        log.info("Test property loaded. jdbcDriver={}, jdbcUrl={}, jdbcUser={}, jdbcPass={}",
                getJdbcDriver(), getJdbcUrl(), getJdbcUserName(), getJdbcPassword().substring(0, 2) + "XXXX");
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(getJdbcDriver());
        driverManagerDataSource.setUrl(getJdbcUrl());
        driverManagerDataSource.setUsername(getJdbcUserName());
        driverManagerDataSource.setPassword(getJdbcPassword());
        return driverManagerDataSource;
    }

    @Bean("jdbcTestTemplate")
    @Profile("test")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(jdbcDataSourceTest());
    }
}
