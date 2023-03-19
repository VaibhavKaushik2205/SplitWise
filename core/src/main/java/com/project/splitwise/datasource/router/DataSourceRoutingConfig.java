package com.project.splitwise.datasource.router;

import com.project.splitwise.datasource.DataSourceConfig;
import com.project.splitwise.datasource.DataSourceType;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceRoutingConfig {

    @Value("${spring.datasource.read-write.url}") private String readWriteDbUrl;
    @Value("${spring.datasource.read-write.username}") private String readWriteDbUsername;
    @Value("${spring.datasource.read-write.password}") private String readWriteDbPassword;
    @Value("${spring.datasource.read-only.url}") private String readOnlyDbUrl;
    @Value("${spring.datasource.read-only.username}") private String readOnlyDbUsername;
    @Value("${spring.datasource.read-only.password}") private String readOnlyDbPassword;

    @Bean
    public DataSource dataSourceConfig() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource readWriteDatasource = readWriteDataSource();
        DataSource readOnlyDatasource = readOnlyDataSource();
        targetDataSources.put(DataSourceType.READ_WRITE,
            readWriteDatasource);
        targetDataSources.put(DataSourceType.READ_ONLY,
            readOnlyDatasource);

        DataSourceRouter datasourceRouter
            = new DataSourceRouter();
        datasourceRouter.setTargetDataSources(targetDataSources);
        datasourceRouter.setDefaultTargetDataSource(readWriteDatasource);
        return datasourceRouter;
    }

    private DataSource readWriteDataSource() {
        return DataSourceConfig.createDataSourceConfig(new DataSourceConfig(readWriteDbUrl,
            readWriteDbUsername, readWriteDbPassword));
    }

    private DataSource readOnlyDataSource() {
        return DataSourceConfig.createDataSourceConfig(new DataSourceConfig(readOnlyDbUrl,
            readOnlyDbUsername, readOnlyDbPassword));
    }
}
