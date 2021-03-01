package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.User;
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
                .withColumnRenamed("log_id", "logId")
                .as(Encoders.bean(User.class));
    }

    public Dataset<User> getDataset(List<Integer> listNumberKey) {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties)
                .where(col("key").isInCollection(listNumberKey))
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
        List<Integer> listUserKey =
                listUserOfResourceData.stream().map(User::getKey).map(Integer::valueOf)
                        .collect(Collectors.toCollection(LinkedList::new));
        Dataset<User> getDatasetOfDb = getUserWithMaxLogIdByKey(listUserKey);
        listUserKey.clear();
        Dataset<Row> datasetLeftResult;

        if(getDatasetOfDb.isEmpty()) {
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

        if(!datasetLeftResult.isEmpty()) {
            datasetLeftResult
                    .select("key", "logId", "name")
                    .withColumnRenamed("logId", "log_id")
                    .toDF()
                    .write()
                    .mode(SaveMode.Append)
                    .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties);
        }
    }

    public Dataset<Row> getGroupedUserMaxLogIdByKey() {
        return getDataset()
                .groupBy(col("key"))
                .agg(max("logId").as("logId"));
    }

    public Dataset<User> getUserWithMaxLogIdByKey(List<Integer> listUserKey) {
        Dataset<Row> groupedUser = getGroupedUserMaxLogIdByKey();
        Dataset<User> datasetUser = getDataset(listUserKey);
        return datasetUser.join(groupedUser,
                datasetUser.col("key").equalTo(groupedUser.col("key"))
                        .and(datasetUser.col("logId").equalTo(groupedUser.col("logId"))))
                .select(datasetUser.col("id"),
                        datasetUser.col("key"),
                        datasetUser.col("logId"),
                        datasetUser.col("name"))
                .as(Encoders.bean(User.class));
    }

    public Row getUserByKey(String userKey) {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties)
                .where(col("key").equalTo(userKey))
                .first();
    }

}
