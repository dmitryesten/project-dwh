package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.User;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;

@Repository
public class UserSparkRepository implements IRepository{

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    public Dataset<User> getDataset() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties)
                .toDF()
                .withColumnRenamed("log_id", "logId")
                .as(Encoders.bean(User.class));
    }

    @Override
    public Collection<? extends EntityDB> get() {
        return getDataset().collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<User> listUserOfResourceData = (List<User>) entities;
        Dataset<User> datasetUsers = sparkSession.createDataset(listUserOfResourceData, Encoders.bean(User.class));
        Dataset<User> getDatasetOfDb = getUserWithMaxLogIdBySourceId();
        Dataset<Row> datasetLeftResult;

        if(getDatasetOfDb.isEmpty()){
            datasetLeftResult = datasetUsers.toDF();
        } else {
            datasetLeftResult = datasetUsers
                    .join(getDatasetOfDb, getDatasetOfDb.col("key").equalTo(datasetUsers.col("key")), "left")
                    .where( (getDatasetOfDb.col("key").isNull())
                            .or(getDatasetOfDb.col("name").notEqual(datasetUsers.col("name"))))
                    .select(datasetUsers.col("key"),
                            datasetUsers.col("logId"),
                            datasetUsers.col("name"));
        }

        datasetLeftResult
                .select("key", "logId", "name")
                .withColumnRenamed("logId", "log_id")
                .toDF()
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties);
    }

    public Dataset<Row> getGroupedUserMaxLogIdByKey() {
        return getDataset()
                .groupBy(col("key"))
                .agg(max("logId").as("logId"));
    }

    public Dataset<User> getUserWithMaxLogIdBySourceId() {
        Dataset<Row> groupedProject = getGroupedUserMaxLogIdByKey();
        Dataset<User> datasetProject = getDataset();
        return datasetProject.join(groupedProject,
                datasetProject.col("key").equalTo(groupedProject.col("key"))
                        .and(datasetProject.col("logId").equalTo(groupedProject.col("logId"))))
                .select(datasetProject.col("id"),
                        datasetProject.col("key"),
                        datasetProject.col("logId"),
                        datasetProject.col("name"))
                .as(Encoders.bean(User.class));
    }

    public Row getUserByKey(String userKey) {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties)
                .where(col("key").equalTo(userKey))
                .first();
    }

}
