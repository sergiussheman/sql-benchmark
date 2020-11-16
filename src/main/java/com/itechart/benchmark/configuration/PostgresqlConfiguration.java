package com.itechart.benchmark.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:application.properties"})
public class PostgresqlConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.postgresql.datasource")
    public DataSource postgresqlDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public JdbcTemplate postgresqlJdbcTemplate(DataSource postgresqlDatasource) {
        return new JdbcTemplate(postgresqlDatasource);
    }
}
