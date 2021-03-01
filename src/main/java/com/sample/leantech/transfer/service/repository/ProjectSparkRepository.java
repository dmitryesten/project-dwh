package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
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
public class ProjectSparkRepository implements IRepository{

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    public Dataset<Project> getDataset() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "projects", postgresProperties)
                .toDF()
                .withColumnRenamed("source_id", "sourceId")
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("create_dt", "createDt")
                .as(Encoders.bean(Project.class));
    }

    public Dataset<Project> getDataset(List<Integer> listSourceIdProject) {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "projects", postgresProperties)
                .where(col("source_id").isInCollection(listSourceIdProject))
                .withColumnRenamed("source_id", "sourceId")
                .withColumnRenamed("log_id", "logId")
                .withColumnRenamed("create_dt", "createDt")
                .as(Encoders.bean(Project.class));
    }

    public Dataset<Project> getDatasetByListId(List<Integer> listId) {
        return getDataset().where(col("sourceId").isInCollection(listId));
    }

    @Override
    public Collection<? extends EntityDB> get() {
        return getDataset().collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Project> projects = (List<Project>) entities;
        Dataset<Project> datasetProject = sparkSession.createDataset(projects, Encoders.bean(Project.class));
        List<Integer> listSourceIdIssue =
                projects.stream().map(Project::getSourceId)
                        .collect(Collectors.toCollection(LinkedList::new));
        projects.clear();
        Dataset<Project> datasetOfDb = getProjectsWithMaxLogIdBySourceId(listSourceIdIssue);
        listSourceIdIssue.clear();
        Dataset<Row> datasetLeftResult;

        if(datasetOfDb.isEmpty()) {
            datasetLeftResult = datasetProject.toDF();
        } else {
            datasetLeftResult = datasetProject
                    .join(datasetOfDb, datasetOfDb.col("sourceId").equalTo(datasetProject.col("sourceId")), "left")
                    .where((datasetOfDb.col("sourceId").isNull())
                            .or(datasetOfDb.col("name").notEqual(datasetProject.col("name")) ))
                    .select(datasetProject.col("logId"),
                            datasetProject.col("sid"),
                            datasetProject.col("sourceId"),
                            datasetProject.col("name"));
        }

        if(!datasetLeftResult.isEmpty()) {
            datasetLeftResult
                    .select("sid", "logId", "sourceId", "name")
                    .withColumnRenamed("sourceId", "source_id")
                    .withColumnRenamed("logId", "log_id")
                    .write()
                    .mode(SaveMode.Append)
                    .jdbc(postgresProperties.getProperty("url"), "projects", postgresProperties);
        }
    }

    public Dataset<Row> getGroupedProjectMaxLogIdBySourceId() {
        return getDataset()
                .groupBy(col("sourceId"))
                .agg(max("logId").as("logId"));
    }

    public Dataset<Project> getProjectsWithMaxLogIdBySourceId(List<Integer> listSourceId) {
        Dataset<Row> groupedProject = getGroupedProjectMaxLogIdBySourceId();
        Dataset<Project> datasetProject = getDatasetByListId(listSourceId);
        return datasetProject.join(groupedProject,
                datasetProject.col("sourceId").equalTo(groupedProject.col("sourceId"))
                        .and(datasetProject.col("logId").equalTo(groupedProject.col("logId"))))
                .select(datasetProject.col("id"),
                        datasetProject.col("sid"),
                        datasetProject.col("logId"),
                        datasetProject.col("sourceId"),
                        datasetProject.col("name"))
                .as(Encoders.bean(Project.class));
    }

}
