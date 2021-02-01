package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.beans.Encoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Slf4j
@Repository
public class ProjectSparkRepository implements IRepository{

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    @Override
    public Collection<? extends EntityDB> get() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "projects", postgresProperties)
                .toDF()
                .withColumnRenamed("source_id", "sourceId")
                .withColumnRenamed("log_id", "logId")
                .as(Encoders.bean(Project.class))
                .collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Project> listProject = (List<Project>) entities;
        Dataset<Project> datasetProject = sparkSession.createDataset(listProject, Encoders.bean(Project.class));
        datasetProject
                .select("sid", "logId", "sourceId", "name")
                .withColumnRenamed("sourceId", "source_id")
                .withColumnRenamed("logId", "log_id")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "projects", postgresProperties);
    }
}
