package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.EntityDB;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.Source;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Repository
public class SourceSparkRepository implements IRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    @Override
    public Collection<? extends EntityDB> get() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "sources", postgresProperties)
                .toDF()
                .as(Encoders.bean(Source.class))
                .collectAsList();
    }

    @Override
    public void save(Collection<? extends EntityDB> entities) {
        List<Source> listSource = (List<Source>) entities;
        Dataset<Source> datasetSource = sparkSession.createDataset(listSource, Encoders.bean(Source.class));

        Dataset<Source> datasetOfDb = sparkSession.createDataset((List<Source>) get(), Encoders.bean(Source.class));
        Dataset<Row> datasetLeftResult = datasetSource
                .join(datasetOfDb, datasetOfDb.col("name").equalTo(datasetSource.col("name")), "left")
                .where(datasetOfDb.col("name").isNull())
                .select(datasetSource.col("id"), datasetSource.col("name"));

        datasetLeftResult.select("name")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "sources", postgresProperties);

    }
}
