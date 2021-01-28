package com.sample.leantech.transfer.config;

import lombok.Setter;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "spark")
public class SparkConfig {

    private String appname;
    private String master;

    @Bean
    public SparkSession sparkSession() {
        return new SparkSession.Builder()
                .appName(appname)
                .master(master)
                .getOrCreate();
    }

}