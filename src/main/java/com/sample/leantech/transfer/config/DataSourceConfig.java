package com.sample.leantech.transfer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "postgres")
public class DataSourceConfig {

    private String url;
    private String driver;
    private String user;
    private String password;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(url)
                .username(user)
                .password(password)
                .build();
    }

}
