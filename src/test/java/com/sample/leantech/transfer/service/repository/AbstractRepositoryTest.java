package com.sample.leantech.transfer.service.repository;

import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"tests", "default"})
public abstract class AbstractRepositoryTest {

    @Autowired
    SparkSession sparkSession;

}