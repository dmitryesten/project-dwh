package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

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

    @Override
    public Collection<? extends EntityDB> get() {
        return getDataset().collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        Dataset<Project> datasetProject = sparkSession.createDataset((List<Project>) entities, Encoders.bean(Project.class));
        Dataset<Project> datasetOfDb = getProjectsWithMaxTimeBySourceId();
        Dataset<Row> datasetLeftResult;

        if(datasetOfDb.isEmpty()) {
            datasetLeftResult = datasetProject.toDF();
        } else {
            datasetLeftResult = datasetProject
                    .join(datasetOfDb, datasetOfDb.col("sourceId").equalTo(datasetProject.col("sourceId")), "left")
                    .where((datasetOfDb.col("sourceId").isNull())
                            .or(datasetOfDb.col("name").notEqual(datasetProject.col("name")) )
                    )
                    .select(datasetProject.col("logId"),
                            datasetProject.col("sid"),
                            datasetProject.col("sourceId"),
                            datasetProject.col("name"));
        }

        datasetLeftResult
                .select("sid", "logId", "sourceId", "name")
                .withColumnRenamed("sourceId", "source_id")
                .withColumnRenamed("logId", "log_id")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "projects", postgresProperties);
    }

    public Dataset<Row> getGroupedProjectMaxTimeBySourceId() {
        return getDataset()
                .groupBy(col("sourceId"))
                .agg(max("createDt").as("createDt"));
    }

    public Dataset<Project> getProjectsWithMaxTimeBySourceId() {
        Dataset<Row> groupedProject = getGroupedProjectMaxTimeBySourceId();
        Dataset<Project> datasetProject = getDataset();
        return datasetProject.join(groupedProject,
                datasetProject.col("sourceId").equalTo(groupedProject.col("sourceId"))
                        .and(datasetProject.col("createDt").equalTo(groupedProject.col("createDt"))))
                .select(datasetProject.col("id"),
                        datasetProject.col("sid"),
                        datasetProject.col("logId"),
                        datasetProject.col("sourceId"),
                        datasetProject.col("name"))
                .as(Encoders.bean(Project.class));
    }
}
