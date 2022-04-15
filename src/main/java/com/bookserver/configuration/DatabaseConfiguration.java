package com.bookserver.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatabaseConfiguration {

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource-bookserver")
    public DataSourceProperties bookserverDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public DataSource bookserverDataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "oauthDataSourceProperties")
    @ConfigurationProperties("spring.datasource-oauth")
    public DataSourceProperties oauthDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "oauthDataSource")
    public DataSource oauthDataSource(
        @Qualifier("oauthDataSourceProperties") final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

}
