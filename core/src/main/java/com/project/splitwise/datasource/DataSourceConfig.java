package com.project.splitwise.datasource;

import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Getter
@AllArgsConstructor
public class DataSourceConfig {

    private String url;
    private String userName;
    private String password;

    public static DataSource createDataSourceConfig(DataSourceConfig datasourceConfig) {
        DataSourceBuilder<?> dbBuilder = DataSourceBuilder.create();
        return dbBuilder.url(datasourceConfig.getUrl())
            .username(datasourceConfig.getUserName())
            .password(datasourceConfig.getPassword())
            .build();
    }
}
