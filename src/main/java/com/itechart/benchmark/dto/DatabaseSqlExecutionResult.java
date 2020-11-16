package com.itechart.benchmark.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
@JsonInclude(Include.NON_NULL)
public class DatabaseSqlExecutionResult {
    private DatabaseVendor databaseVendor;
    private Result result;
    private Long executionTime;
    private TimeUnit executionTimeUnit;
    private String errorMessage;

    public static DatabaseSqlExecutionResult success(DatabaseVendor databaseVendor, long executionTime, TimeUnit timeUnit) {
        DatabaseSqlExecutionResult executionResult = new DatabaseSqlExecutionResult();
        executionResult.setDatabaseVendor(databaseVendor);
        executionResult.setResult(Result.SUCCESS);
        executionResult.setExecutionTime(executionTime);
        executionResult.setExecutionTimeUnit(timeUnit);
        return executionResult;
    }

    public static DatabaseSqlExecutionResult failed(DatabaseVendor databaseVendor, String errorMessage) {
        DatabaseSqlExecutionResult executionResult = new DatabaseSqlExecutionResult();
        executionResult.setDatabaseVendor(databaseVendor);
        executionResult.setResult(Result.FAILED);
        executionResult.setErrorMessage(errorMessage);
        return executionResult;
    }

    public enum Result {
        SUCCESS,
        FAILED
    }
}
