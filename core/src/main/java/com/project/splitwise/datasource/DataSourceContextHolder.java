package com.project.splitwise.datasource;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

@UtilityClass
public class DataSourceContextHolder {

    private static final ThreadLocal<DataSourceType> CONTEXT =
        new ThreadLocal<>();

    public static void set(DataSourceType dataSourceType) {
        Assert.notNull(dataSourceType, "clientDatabase cannot be null");
        CONTEXT.set(dataSourceType);
    }

    public static DataSourceType getSourceDatabase() {
        return CONTEXT.get();
    }

    public static void setDefault() {
        // Should set default to MASTER or clear the context?
        CONTEXT.set(DataSourceType.READ_WRITE);
    }

}
