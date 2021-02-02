package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.db.Worklog;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Repository
public class IssueSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    @Override
    public Collection<? extends EntityDB> get() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties)
                .toDF()
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("source_id", "sourceId")
                .as(Encoders.bean(Issue.class))
                .collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Issue> listIssue = (List<Issue>) entities;
        Dataset<Issue> datasetProject = sparkSession.createDataset(listIssue, Encoders.bean(Issue.class));
        datasetProject
                .select("pid", "sid", "logId", "hid", "sourceId", "type", "name", "summery")
                .withColumnRenamed("logId", "log_id")
                .withColumnRenamed("sourceId", "source_id")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties);
    }
}