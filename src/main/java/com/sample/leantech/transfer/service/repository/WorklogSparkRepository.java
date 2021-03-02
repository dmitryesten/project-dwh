package com.sample.leantech.transfer.service.repository;


import com.sample.leantech.transfer.model.db.*;
import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Worklog;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;


@Slf4j
@Repository
public class WorklogSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    @Autowired
    IssueSparkRepository issueSparkRepository;

    @Autowired
    UserSparkRepository userSparkRepository;

    public Dataset<Worklog> getDataset() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "worklogs", postgresProperties)
                .toDF()
                .withColumnRenamed("issue_id", "issueId")
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("source_id", "sourceId")
                .withColumnRenamed("updated_dt", "updated")
                .withColumnRenamed("date", "started")
                .withColumnRenamed("time_spent", "timeSpentSecond")
                .withColumnRenamed("user_id", "userId")
                .as(Encoders.bean(Worklog.class));
    }

    public Dataset<Worklog> getDataset(List<Integer> listSourceIdWorklog) {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "worklogs", postgresProperties)
                .toDF()
                .where(col("source_id").isInCollection(listSourceIdWorklog))
                .withColumnRenamed("issue_id", "issueId")
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("source_id", "sourceId")
                .withColumnRenamed("updated_dt", "updated")
                .withColumnRenamed("date", "started")
                .withColumnRenamed("time_spent", "timeSpentSecond")
                .withColumnRenamed("user_id", "userId")
                .as(Encoders.bean(Worklog.class));
    }

    @Override
    public Collection<? extends EntityDB> get() {
        return getDataset().collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Worklog> listWorklogs = (List<Worklog>) entities;
        Dataset<Worklog> datasetWorklog = sparkSession.createDataset(listWorklogs, Encoders.bean(Worklog.class));

        List<Integer> listSourceIdWorklog =
                listWorklogs.stream().map(Worklog::getSourceId).collect(Collectors.toCollection(LinkedList::new));
        Dataset<Worklog> datasetWorklogOfDb = getWorklogWithMaxLogIdBySourceId(listSourceIdWorklog);
        listSourceIdWorklog.clear();

        List<Integer> listSourceIdIssues =
                listWorklogs.stream().map(Worklog::getIssueId)
                        .collect(Collectors.toCollection(LinkedList::new));
        Dataset<Issue> datasetIssueOfDb = issueSparkRepository.getDataset(listSourceIdIssues);
        listSourceIdIssues.clear();

        List<Integer> listUserKey =
                listWorklogs.stream().map(Worklog::getUserId)
                        .collect(Collectors.toCollection(LinkedList::new));
        Dataset<User> datasetUser = userSparkRepository.getUserWithMaxLogIdByKey(listUserKey);
        listUserKey.clear();

        Dataset<Row> datasetLeftResult;

        if(datasetWorklogOfDb.isEmpty()) {
            log.info("datasetWorklogOfDb is empty of database");
            datasetLeftResult =
                    datasetWorklog.join(datasetIssueOfDb,
                            datasetWorklog.col("issueId").equalTo(datasetIssueOfDb.col("sourceId")) )
                            .select(datasetIssueOfDb.col("id").as("issueId"),
                                    datasetWorklog.col("logId"),
                                    datasetWorklog.col("sid"),
                                    datasetWorklog.col("sourceId"),
                                    datasetWorklog.col("updated"),
                                    datasetWorklog.col("started"),
                                    datasetWorklog.col("timeSpentSecond"),
                                    datasetWorklog.col("username"),
                                    datasetWorklog.col("userId"));
        } else {
            log.info("Dataset of DB is not empty");
            datasetLeftResult = datasetWorklog.join(datasetWorklogOfDb,
                    datasetWorklog.col("sourceId").equalTo(datasetWorklogOfDb.col("sourceId")), "left")
                    .where((datasetWorklogOfDb.col("sourceId").isNull())
                            .or(datasetWorklogOfDb.col("timeSpentSecond").notEqual(datasetWorklog.col("timeSpentSecond")))
                            .or(datasetWorklogOfDb.col("updated").notEqual(datasetWorklog.col("updated"))))
                    .select(datasetWorklog.col("issueId"),
                            datasetWorklog.col("logId"),
                            datasetWorklog.col("sid"),
                            datasetWorklog.col("sourceId"),
                            datasetWorklog.col("updated"),
                            datasetWorklog.col("started"),
                            datasetWorklog.col("timeSpentSecond"),
                            datasetWorklog.col("username"),
                            datasetWorklog.col("userId"));
            datasetLeftResult.show();

            datasetLeftResult =
                    datasetLeftResult.join(datasetIssueOfDb,
                            datasetWorklog.col("issueId").equalTo(datasetIssueOfDb.col("sourceId")) )
                            .select(datasetIssueOfDb.col("id").as("issueId"),
                                    datasetWorklog.col("logId"),
                                    datasetWorklog.col("sid"),
                                    datasetWorklog.col("sourceId"),
                                    datasetWorklog.col("updated"),
                                    datasetWorklog.col("started"),
                                    datasetWorklog.col("timeSpentSecond"),
                                    datasetWorklog.col("username"),
                                    datasetWorklog.col("userId"));
        }

        if(!datasetLeftResult.isEmpty()) {
            datasetLeftResult
                    .select("issueId", "logId", "sid", "sourceId", "updated", "started", "timeSpentSecond", "username", "userId")
                    .withColumnRenamed("issueId", "issue_id")
                    .withColumnRenamed("logId", "log_id")
                    .withColumnRenamed("sourceId", "source_id")
                    .withColumnRenamed("updated", "updated_dt")
                    .withColumnRenamed("started", "date")
                    .withColumnRenamed("timeSpentSecond", "time_spent")
                    .withColumnRenamed("userId", "user_id")
                    .write()
                    .mode(SaveMode.Append)
                    .jdbc(postgresProperties.getProperty("url"), "worklogs", postgresProperties);
        }
    }

    public Dataset<Row> getGroupedWorklogMaxLogIdBySourceId() {
        return getDataset()
                .groupBy(col("sourceId"))
                .agg(max("logId").as("logId"));
    }

    public Dataset<Worklog> getWorklogWithMaxLogIdBySourceId(List<Integer> listSourceIdWorklog) {
        Dataset<Row> groupedWorklog = getGroupedWorklogMaxLogIdBySourceId();
        Dataset<Worklog> datasetProject = getDataset(listSourceIdWorklog);
        return datasetProject.join(groupedWorklog,
                datasetProject.col("sourceId").equalTo(groupedWorklog.col("sourceId"))
                        .and(datasetProject.col("logId").equalTo(groupedWorklog.col("logId"))))
                .select(datasetProject.col("id"),
                        datasetProject.col("issueId"),
                        datasetProject.col("logId"),
                        datasetProject.col("sid"),
                        datasetProject.col("sourceId"),
                        datasetProject.col("updated"),
                        datasetProject.col("started"),
                        datasetProject.col("timeSpentSecond"),
                        datasetProject.col("username"),
                        datasetProject.col("userId"))
                .as(Encoders.bean(Worklog.class));
    }

}
