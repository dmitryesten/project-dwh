package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
@Slf4j
@Repository
public class IssueSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    @Autowired
    ProjectSparkRepository projectSparkRepository;

    public Dataset<Issue> getDataset(){
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties)
                .toDF()
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("source_id", "sourceId")
                .as(Encoders.bean(Issue.class));
    }

    @Override
    public Collection<? extends EntityDB> get() {
        return getDataset().collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Issue> listIssue = (List<Issue>) entities;
        Dataset<Issue> datasetIssue = sparkSession.createDataset(listIssue, Encoders.bean(Issue.class));
        Dataset<Issue> datasetIssueOfDb = getDataset();
        Dataset<Project> datasetProject = projectSparkRepository.getDataset();
        datasetProject.show();
        Dataset<Row> datasetLeftResult;


        if(datasetIssueOfDb.isEmpty()){
            log.info("Dataset Issue is empty in database");
            datasetLeftResult = datasetIssue.toDF()
                            .join(datasetProject, datasetProject.col("sourceId").equalTo(datasetIssue.col("pid")))
                            .select(datasetProject.col("id").as("pid"),
                                    datasetIssue.col("sid"),
                                    datasetIssue.col("logId"),
                                    datasetIssue.col("sourceId"),
                                    datasetIssue.col("hid"),
                                    datasetIssue.col("type"),
                                    datasetIssue.col("name"),
                                    datasetIssue.col("summery"));

        } else {
            datasetLeftResult = datasetIssue
                    .join(datasetIssueOfDb,
                            datasetIssueOfDb.col("sourceId").equalTo(datasetIssue.col("sourceId")),
                            "left")
                    .where((datasetIssueOfDb.col("sourceId").isNull())
                    ).select(datasetIssue.col("pid"),
                            datasetIssue.col("sid"),
                            datasetIssue.col("logId"),
                            datasetIssue.col("sourceId"),
                            datasetIssue.col("hid"),
                            datasetIssue.col("type"),
                            datasetIssue.col("name"),
                            datasetIssue.col("summery"));
            /*
            .or(datasetIssueOfDb.col("hid").notEqual(datasetIssue.col("hid"))
                                    .and(datasetIssueOfDb.col("sourceId").equalTo(datasetIssueOfDb.col("sourceId"))))
                            .or(datasetIssueOfDb.col("type").notEqual(datasetIssue.col("type"))
                                    .and(datasetIssueOfDb.col("sourceId").equalTo(datasetIssueOfDb.col("sourceId"))))
                            .or(datasetIssueOfDb.col("name").notEqual(datasetIssue.col("name"))
                                    .and(datasetIssueOfDb.col("sourceId").equalTo(datasetIssueOfDb.col("sourceId"))))
                            .or(datasetIssueOfDb.col("summery").notEqual(datasetIssue.col("summery"))
                                    .and(datasetIssueOfDb.col("sourceId").equalTo(datasetIssueOfDb.col("sourceId"))))
             */
            datasetLeftResult =
                    datasetLeftResult
                    .join(datasetProject, datasetProject.col("sourceId").equalTo(datasetIssue.col("pid")))
                    .select(datasetProject.col("id"),
                            datasetIssue.col("sid"),
                            datasetIssue.col("logId"),
                            datasetIssue.col("sourceId"),
                            datasetIssue.col("hid"),
                            datasetIssue.col("type"),
                            datasetIssue.col("name"),
                            datasetIssue.col("summery"))
                    .withColumnRenamed("id", "pid");
        }

        datasetLeftResult
                .select("pid", "sid", "logId", "hid", "sourceId", "type", "name", "summery")
                .withColumnRenamed("logId", "log_id")
                .withColumnRenamed("sourceId", "source_id")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties);
    }
}
