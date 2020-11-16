package com.itechart.benchmark.controller;

import com.itechart.benchmark.dto.DatabaseSqlExecutionResult;
import com.itechart.benchmark.service.DatabaseBenchmarkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/benchmark")
public class BenchmarkController {
    private final DatabaseBenchmarkService benchmarkService;


    public BenchmarkController(DatabaseBenchmarkService benchmarkService) {
        this.benchmarkService = benchmarkService;
    }

    @PostMapping
    public List<DatabaseSqlExecutionResult> executeSqlQuery(@RequestBody @NotEmpty @NotBlank String sqlQuery) {
        return benchmarkService.executeQuery(sqlQuery);
    }
}
