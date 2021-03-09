package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.IssueField;
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

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;

@Slf4j
@Repository
public class IssueFieldSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    @Autowired
    private IssueSparkRepository issueSparkRepository;

    @Override
    public Collection<? extends EntityDB> get() {
        return null;
    }

    public Dataset<IssueField> getDataset() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "issue_fields", postgresProperties)
                .withColumnRenamed("issue_id", "issueId")
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("issue_source_id", "issueSourceId")
                .withColumnRenamed("create_dt", "createDt")
                .as(Encoders.bean(IssueField.class));
    }

    public Dataset<IssueField> getDatasetByListId(List<Integer> listId) {
        return getDataset().where(col("issueSourceId").isInCollection(listId));
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<IssueField> listIssueField = (List<IssueField>) entities;
        Dataset<IssueField> datasetIssueField = sparkSession.createDataset(listIssueField, Encoders.bean(IssueField.class));

        List<Integer> listSourceIdIssues =
                listIssueField.stream().map(IssueField::getIssueSourceId).collect(Collectors.toCollection(LinkedList::new));
        Dataset<Issue> datasetIssueDb = issueSparkRepository.getIssueWithMaxLogIdBySourceId(listSourceIdIssues);

        Dataset<IssueField> datasetIssueFieldOfDb = getIssueFieldWithMaxLogIdBySourceId(listSourceIdIssues);

        Dataset<Row> datasetLeftResult = null;

        if(datasetIssueFieldOfDb.isEmpty()) {
            datasetLeftResult =
                   datasetIssueField.toDF()
                   .join(datasetIssueDb, datasetIssueField.col("issueSourceId").equalTo(datasetIssueDb.col("sourceId")))
                   .select(datasetIssueDb.col("id").as("issueId"),
                           datasetIssueField.col("sid"),
                           datasetIssueField.col("logId"),
                           datasetIssueField.col("issueSourceId"),
                           datasetIssueField.col("field"),
                           datasetIssueField.col("type"),
                           datasetIssueField.col("name"),
                           datasetIssueField.col("value"));

        } else {
            datasetLeftResult = datasetIssueField
                    .join(datasetIssueFieldOfDb,
                            datasetIssueFieldOfDb.col("issueSourceId").equalTo(datasetIssueField.col("issueSourceId")),
                            "left")
                    .where((datasetIssueFieldOfDb.col("issueSourceId").isNull())
                            .or(datasetIssueFieldOfDb.col("value").notEqual(datasetIssueField.col("value"))
                                .or(datasetIssueFieldOfDb.col("name").notEqual(datasetIssueField.col("name")) )) )
                    .select(datasetIssueField.col("issueId"),
                            datasetIssueField.col("sid"),
                            datasetIssueField.col("logId"),
                            datasetIssueField.col("issueSourceId"),
                            datasetIssueField.col("field"),
                            datasetIssueField.col("type"),
                            datasetIssueField.col("name"),
                            datasetIssueField.col("value"));

            datasetLeftResult = datasetLeftResult.toDF()
                            .join(datasetIssueDb,
                                    datasetIssueField.col("issueSourceId").equalTo(datasetIssueDb.col("sourceId")))
                            .select(datasetIssueDb.col("id").as("issueId"),
                                    datasetIssueField.col("sid"),
                                    datasetIssueField.col("logId"),
                                    datasetIssueField.col("issueSourceId"),
                                    datasetIssueField.col("field"),
                                    datasetIssueField.col("type"),
                                    datasetIssueField.col("name"),
                                    datasetIssueField.col("value"));
        }

        if(!datasetLeftResult.isEmpty()) {
            datasetLeftResult
                    .select("issueId", "sid", "logId", "issueSourceId", "field", "type", "name", "value")
                    .withColumnRenamed("issueId", "issue_id")
                    .withColumnRenamed("logId", "log_id")
                    .withColumnRenamed("issueSourceId", "issue_source_id")
                    .write()
                    .mode(SaveMode.Append)
                    .jdbc(postgresProperties.getProperty("url"), "issue_fields", postgresProperties);
        }

    }

    public Dataset<Row> getGroupedIssueFieldMaxLogIdBySourceId() {
        return getDataset()
                .groupBy(col("issueSourceId"), col("name"))
                .agg(max("logId").as("logId"));
    }

    public Dataset<IssueField> getIssueFieldWithMaxLogIdBySourceId(List<Integer> listSourceId) {
        Dataset<Row> groupedIssueFields = getGroupedIssueFieldMaxLogIdBySourceId();
        Dataset<IssueField> filteredIssueFields = getDatasetByListId(listSourceId);
        return filteredIssueFields.join(groupedIssueFields,
                filteredIssueFields.col("issueSourceId").equalTo(groupedIssueFields.col("issueSourceId"))
                        .and(filteredIssueFields.col("logId").equalTo(groupedIssueFields.col("logId"))))
                .select(filteredIssueFields.col("id"),
                        filteredIssueFields.col("issueId"),
                        filteredIssueFields.col("sid"),
                        filteredIssueFields.col("logId"),
                        filteredIssueFields.col("issueSourceId"),
                        filteredIssueFields.col("field"),
                        filteredIssueFields.col("type"),
                        filteredIssueFields.col("name"),
                        filteredIssueFields.col("value"),
                        filteredIssueFields.col("createDt"))
                .as(Encoders.bean(IssueField.class));
    }

}
