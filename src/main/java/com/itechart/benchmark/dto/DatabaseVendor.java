package com.itechart.benchmark.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DatabaseVendor {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL"),
    MARIADB("MariaDB");

    private final String formattedName;

    DatabaseVendor(String formattedName) {
        this.formattedName = formattedName;
    }

    @JsonValue
    public String getFormattedName() {
        return formattedName;
    }
}
