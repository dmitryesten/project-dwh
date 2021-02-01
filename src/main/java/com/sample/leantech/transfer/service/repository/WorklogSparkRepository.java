package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.db.Worklog;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class WorklogSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;


    @Override
    public Collection<? extends EntityDB> get() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "worklogs", postgresProperties)
                .toDF()
                .withColumnRenamed("issue_id", "issueId")
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("source_id", "sourceId")
                .withColumnRenamed("updated_dt", "updated")
                .withColumnRenamed("time_spent", "timeSpentSecond")
                .withColumnRenamed("user_id", "userId")
                .as(Encoders.bean(Worklog.class))
                .collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Worklog> list = (List<Worklog>) entities;
        Dataset<Worklog> datasetProject = sparkSession.createDataset(list, Encoders.bean(Worklog.class));
        datasetProject
                .select("issueId", "logId", "sid", "sourceId", "updated", "timeSpentSecond", "username", "userId")
                .withColumnRenamed("issueId", "issue_id")
                .withColumnRenamed("logId", "log_id")
                .withColumnRenamed("sourceId", "source_id")
                .withColumnRenamed("updated", "updated_dt")
                .withColumnRenamed("timeSpentSecond", "time_spent")
                .withColumnRenamed("userId", "user_id")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "worklogs", postgresProperties);
    }
}
