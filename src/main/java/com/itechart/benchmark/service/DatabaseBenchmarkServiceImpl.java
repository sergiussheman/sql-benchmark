package com.itechart.benchmark.service;

import com.itechart.benchmark.dto.DatabaseSqlExecutionResult;
import com.itechart.benchmark.dto.DatabaseVendor;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

@Service
public class DatabaseBenchmarkServiceImpl implements DatabaseBenchmarkService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseBenchmarkServiceImpl.class);

    private final List<JdbcTemplate> jdbcTemplates;

    private ExecutorService executorService;
    // We want to handle incoming sql queries in FIFO manner.
    // For that purpose we set 'fair' property of ReentrantReadWriteLock to true
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final WriteLock writeLock = readWriteLock.writeLock();

    public DatabaseBenchmarkServiceImpl(List<JdbcTemplate> jdbcTemplates) {
        this.jdbcTemplates = jdbcTemplates;
    }

    @PostConstruct
    public void init() {
        this.executorService = Executors.newFixedThreadPool(jdbcTemplates.size());
    }

    @Override
    public List<DatabaseSqlExecutionResult> executeQuery(String sql) {
        try {
            writeLock.lock();

            List<Future<DatabaseSqlExecutionResult>> futures = new ArrayList<>();
            for (JdbcTemplate jdbcTemplate : jdbcTemplates) {
                DatabaseVendor vendor = determineDatabaseName(jdbcTemplate);
                SqlExecutor sqlExecutorTask = new SqlExecutor(vendor, sql, jdbcTemplate);
                futures.add(executorService.submit(sqlExecutorTask));
            }

            List<DatabaseSqlExecutionResult> result = new ArrayList<>();
            try {
                for (Future<DatabaseSqlExecutionResult> future : futures) {
                    result.add(future.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Concurrency exception was caught", e);
                throw new RuntimeException(e);
            }

            return result;
        } finally {
            writeLock.unlock();
        }
    }

    private DatabaseVendor determineDatabaseName(JdbcTemplate jdbcTemplate) {
        HikariDataSource dataSource = (HikariDataSource) jdbcTemplate.getDataSource();
        if (dataSource != null) {
            return DatabaseVendor.valueOf(dataSource.getUsername().toUpperCase());
        }

        return null;
    }

    public static class SqlExecutor implements Callable<DatabaseSqlExecutionResult> {
        private final DatabaseVendor databaseVendor;
        private final String sqlQuery;
        private final JdbcTemplate jdbcTemplate;

        public SqlExecutor(DatabaseVendor databaseVendor, String sqlQuery, JdbcTemplate jdbcTemplate) {
            this.databaseVendor = databaseVendor;
            this.sqlQuery = sqlQuery;
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override public DatabaseSqlExecutionResult call() {
            try {
                long startTime = System.nanoTime();

                // execute some sql query against database
                jdbcTemplate.execute(sqlQuery);

                long endTime = System.nanoTime();
                long executionTime = endTime - startTime;

                return DatabaseSqlExecutionResult.success(databaseVendor, executionTime, TimeUnit.NANOSECONDS);
            } catch (Exception e) {
                return DatabaseSqlExecutionResult.failed(databaseVendor, e.getMessage());
            }
        }
    }
}
