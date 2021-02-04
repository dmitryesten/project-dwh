package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.LogTransfer;
import com.sample.leantech.transfer.model.db.Source;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Repository
public class LogTransferSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    @Override
    public Collection<? extends EntityDB> get() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "logs", postgresProperties)
                .toDF()
                .withColumnRenamed("start_dt", "startDt")
                .withColumnRenamed("end_dt", "endDt")
                .as(Encoders.bean(LogTransfer.class))
                .collectAsList();
    }


    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<LogTransfer> listLogTransfer = (List<LogTransfer>) entities;
        Dataset<LogTransfer> datasetTransfer = sparkSession.createDataset(listLogTransfer, Encoders.bean(LogTransfer.class));
        datasetTransfer.select("sid","startDt")
                .withColumnRenamed("startDt", "start_dt")
                .withColumnRenamed("endDt", "end_dt")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "logs", postgresProperties);

    }



}
