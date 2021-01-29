package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.User;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Repository
public class IssueSparkRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    public void save(List<Issue> issues) {
        Dataset<Issue> datasetProject = sparkSession.createDataset(issues, Encoders.bean(Issue.class));
        datasetProject
                .select("pid", "sid", "logId", "hid", "sourceId", "type", "name", "summery")
                .withColumnRenamed("logId", "log_id")
                .withColumnRenamed("sourceId", "source_id")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties);
    }

    public List<Issue> getIssues() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties)
                .toDF()
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("source_id", "sourceId")
                .as(Encoders.bean(Issue.class))
                .collectAsList();
    }




}
