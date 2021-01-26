package com.sample.leantech.transfer.config;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Value("${spark.app.name}")
    private String sparkName;
    @Value("${spark.master}")
    private String sparkMaster;

    @Bean
    public SparkConf sparkConf(){
        return new SparkConf()
                .setAppName(sparkName)
                .setMaster(sparkMaster);
    }

    @Bean
    public SparkContext sparkContext() {
        return new SparkContext(sparkConf());
    }

}