package com.itechart.benchmark.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:application.properties"})
public class MariaDbConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.mariadb.datasource")
    public DataSource mariaDbDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate mariaDbJdbcTemplate(@Qualifier("mariaDbDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
