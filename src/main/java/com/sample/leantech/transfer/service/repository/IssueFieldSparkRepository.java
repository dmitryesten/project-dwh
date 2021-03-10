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

import static org.apache.spark.sql.functions.*;

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
                   .select(datasetIssueDb.col("id").as("issueId")
                           ,datasetIssueField.col("sid")
                           ,datasetIssueField.col("logId")
                           ,datasetIssueField.col("issueSourceId")
                           ,datasetIssueField.col("field")
                           ,datasetIssueField.col("type")
                           ,datasetIssueField.col("name")
                           ,datasetIssueField.col("value"));

        } else {
            Dataset<Row> datasetWithUniqueConcatRowOfSourceList =
                    datasetIssueField.toDF().withColumn("concatSourceOfSourceList",
                            concat(datasetIssueField.toDF().col("issueSourceId")
                                    ,datasetIssueField.toDF().col("field")
                                    ,datasetIssueField.toDF().col("type")
                                    ,datasetIssueField.toDF().col("name")
                                    ));
            //datasetWithUniqueConcatRowOfSourceList.show();

            Dataset<Row> datasetWithUniqueConcatRowOfDb =
                    datasetIssueFieldOfDb.toDF().withColumn("concatSourceOfDb",
                    concat(datasetIssueFieldOfDb.col("issueSourceId")
                            ,datasetIssueFieldOfDb.col("field")
                            ,datasetIssueFieldOfDb.col("type")
                            ,datasetIssueFieldOfDb.col("name")
                            ));


            datasetLeftResult = datasetWithUniqueConcatRowOfSourceList
                    .join(datasetWithUniqueConcatRowOfDb,
                            datasetWithUniqueConcatRowOfSourceList.col("concatSourceOfSourceList").equalTo(
                                    datasetWithUniqueConcatRowOfDb.col("concatSourceOfDb")),
                            "left")
                    .where((datasetWithUniqueConcatRowOfDb.col("issueSourceId").isNull())
                            .or(datasetWithUniqueConcatRowOfDb.col("value").notEqual(
                                    datasetWithUniqueConcatRowOfSourceList.col("value")) ) )
                    .select(datasetWithUniqueConcatRowOfSourceList.col("issueId")
                            ,datasetWithUniqueConcatRowOfSourceList.col("sid")
                            ,datasetWithUniqueConcatRowOfSourceList.col("logId")
                            ,datasetWithUniqueConcatRowOfSourceList.col("issueSourceId")
                            ,datasetWithUniqueConcatRowOfSourceList.col("field")
                            ,datasetWithUniqueConcatRowOfSourceList.col("type")
                            ,datasetWithUniqueConcatRowOfSourceList.col("name")
                            ,datasetWithUniqueConcatRowOfSourceList.col("value"));
            datasetLeftResult.show();
            System.out.println("TEST_PRINT3");

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

            datasetLeftResult.show();
            System.out.println("TEST_PRINT4");
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
        Dataset<Row> datasetWithConcatGrouped = getDataset().toDF()
                .withColumn("concatGroupedSourceOfDb",
                        concat(col("issueSourceId")
                                ,col("field")
                                ,col("type")
                                ,col("name")));

        return datasetWithConcatGrouped
                .groupBy(col("concatGroupedSourceOfDb"))
                .agg(max("logId").as("logId"));
    }

    public Dataset<IssueField> getIssueFieldWithMaxLogIdBySourceId(List<Integer> listSourceId) {
        Dataset<Row> groupedIssueFields = getGroupedIssueFieldMaxLogIdBySourceId();
        Dataset<Row> filteredIssueFields = getDatasetByListId(listSourceId).toDF()
                .withColumn("concatSourceOfSourceList",
                concat(col("issueSourceId")
                        ,col("field")
                        ,col("type")
                        ,col("name")
                ));
        return filteredIssueFields.join(groupedIssueFields,
                filteredIssueFields.col("concatSourceOfSourceList").equalTo(groupedIssueFields.col("concatGroupedSourceOfDb"))
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
