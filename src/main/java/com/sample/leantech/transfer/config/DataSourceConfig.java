package com.sample.leantech.transfer.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "postgres")
public class DataSourceConfig {

    private String url;
    private String driver;
    private String user;
    private String password;

    @Bean(name =  "dataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(url)
                .username(user)
                .password(password)
                .build();

    }
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource, true);
    }

    @Bean
    public Properties getPostgresProperties() {
        Properties properties = new Properties();
            properties.setProperty("driver", "org.postgresql.Driver");
            //IF you run from docker-compose that rename url as jdbc:postgresql://db:5432/project_dwh
            properties.setProperty("url", "jdbc:postgresql://localhost:5432/project_dwh");
            properties.setProperty("user", "spring");
            properties.setProperty("password", "123");
        return properties;
    }

}
