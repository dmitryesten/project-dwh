package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.Source;
import com.sample.leantech.transfer.model.db.User;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

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
        Dataset<User> getDatasetOfDb = getDataset();
        Dataset<Row> datasetLeftResult;

        if(getDatasetOfDb.isEmpty()){
            datasetLeftResult = datasetUsers.toDF();
        } else {
            datasetLeftResult = datasetUsers
                    .join(getDatasetOfDb, getDatasetOfDb.col("name").equalTo(datasetUsers.col("name")), "left")
                    .where(getDatasetOfDb.col("name").isNull())
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

}
