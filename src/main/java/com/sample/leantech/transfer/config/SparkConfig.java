package com.sample.leantech.transfer.config;

import lombok.Setter;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SparkConf sparkConf(){
        return new SparkConf()
                .setAppName(appname)
                .setMaster(master);
    }

    @Bean
    public SparkContext sparkContext(@Autowired SparkConf sparkConf) {
        return new SparkContext(sparkConf);
    }

}