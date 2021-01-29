package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.Source;
import com.sample.leantech.transfer.model.db.User;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Properties;

@Repository
public class UserSparkRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    public List<User> getUsers() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties)
                .toDF()
                .withColumnRenamed("log_id", "logId")
                .as(Encoders.bean(User.class))
                .collectAsList();
    }

    public void save(List<User> user){
        Dataset<User> datasetProject = sparkSession.createDataset(user, Encoders.bean(User.class));
        datasetProject
                .select("key", "logId", "name")
                .withColumnRenamed("logId", "log_id")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "users", postgresProperties);
    }


}
