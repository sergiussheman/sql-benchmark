package com.itechart.benchmark.service;

import com.itechart.benchmark.dto.DatabaseSqlExecutionResult;

import java.util.List;

public interface DatabaseBenchmarkService {
    List<DatabaseSqlExecutionResult> executeQuery(String sql);
}
