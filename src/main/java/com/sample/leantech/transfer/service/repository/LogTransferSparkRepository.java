package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.LogTransfer;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;

@Repository
public class LogTransferSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    public Dataset<LogTransfer> getDataset() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "logs", postgresProperties)
                .toDF()
                .withColumnRenamed("start_dt", "startDt")
                .withColumnRenamed("end_dt", "endDt")
                .as(Encoders.bean(LogTransfer.class));
    }

    @Override
    public Collection<? extends EntityDB> get() {
        return getDataset().collectAsList();
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

    protected Row getIdMaxOpenLog() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "logs", postgresProperties)
                .toDF()
                .withColumnRenamed("start_dt", "startDt")
                .withColumnRenamed("end_dt", "endDt")
                .where((col("endDt").isNull()).and(col("result").isNull()))
                .agg(max("id")).first();
    }

    public Dataset<Row> getOpenLogTransfer() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "logs", postgresProperties)
                .where(col("id").equalTo(getIdMaxOpenLog().get(0)));
    }

    public void closeOpenedLogTransfer(LogTransfer logTransfer) {
        Dataset<LogTransfer> datasetCloseLogTransfer = sparkSession.createDataset(List.of(logTransfer),
                Encoders.bean(LogTransfer.class));
        datasetCloseLogTransfer.select("hid", "sid", "endDt", "result")
                .withColumnRenamed("endDt", "end_dt")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "logs", postgresProperties);
    }

}