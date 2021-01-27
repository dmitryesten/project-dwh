package com.sample.leantech.transfer.config;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
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
    public SparkSession sparkSession(){
        return new SparkSession.Builder()
                .appName("Consumer json of Jira")
                .master("local")
                .getOrCreate();
    }



}