package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.functions.col;

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

    public Dataset<Issue> getDataset() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties)
                .toDF()
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("source_id", "sourceId")
                .withColumnRenamed("create_dt", "createDt")
                .as(Encoders.bean(Issue.class));
    }

    public Dataset<Issue> getDatasetByListId(List<Integer> listId) {
        return getDataset().where(col("sourceId").isInCollection(listId));
    }

    @Override
    public Collection<? extends EntityDB> get() {
        return getDataset().collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Issue> issues = (List<Issue>) entities;
        Dataset<Issue> datasetIssue = sparkSession.createDataset(issues, Encoders.bean(Issue.class));
        List<Integer> listSourceIdIssue =
                issues.stream().map(Issue::getSourceId)
                        .collect(Collectors.toCollection(LinkedList::new));
        issues.clear();
        Dataset<Issue> datasetIssueOfDb = getIssueWithMaxLogIdBySourceId(listSourceIdIssue);
        listSourceIdIssue.clear();
        Dataset<Project> datasetProject = projectSparkRepository.getDataset();
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
                            .or(datasetIssueOfDb.col("summery").notEqual(datasetIssue.col("summery"))
                            .or(datasetIssueOfDb.col("type").notEqual(datasetIssue.col("type"))
                            .or(datasetIssueOfDb.col("name").notEqual(datasetIssue.col("name")) )))
                    ).select(datasetIssue.col("pid"),
                            datasetIssue.col("sid"),
                            datasetIssue.col("logId"),
                            datasetIssue.col("sourceId"),
                            datasetIssue.col("hid"),
                            datasetIssue.col("type"),
                            datasetIssue.col("name"),
                            datasetIssue.col("summery"));

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

        if(!datasetLeftResult.isEmpty()) {
            datasetLeftResult
                    .select("pid", "sid", "logId", "hid", "sourceId", "type", "name", "summery")
                    .withColumnRenamed("logId", "log_id")
                    .withColumnRenamed("sourceId", "source_id")
                    .write()
                    .mode(SaveMode.Append)
                    .jdbc(postgresProperties.getProperty("url"), "issues", postgresProperties);
        }

    }

    public Dataset<Row> getGroupedIssueMaxLogIdBySourceId() {
        return getDataset()
                .groupBy(col("sourceId"))
                .agg(max("logId").as("logId"));
    }

    public Dataset<Issue> getIssueWithMaxLogIdBySourceId(List<Integer> listSourceId) {
        Dataset<Row> groupedIssues = getGroupedIssueMaxLogIdBySourceId();
        Dataset<Issue> issues = getDatasetByListId(listSourceId);
        return issues.join(groupedIssues,
                        issues.col("sourceId").equalTo(groupedIssues.col("sourceId"))
                        .and(issues.col("logId").equalTo(groupedIssues.col("logId"))))
                .select(issues.col("id"),
                        issues.col("pid"),
                        issues.col("sid"),
                        issues.col("logId"),
                        issues.col("sourceId"),
                        issues.col("hid"),
                        issues.col("type"),
                        issues.col("name"),
                        issues.col("summery"),
                        issues.col("createDt"))
                .as(Encoders.bean(Issue.class));
    }

}
