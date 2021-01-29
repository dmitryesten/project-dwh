package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.Source;
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
public class SourceSparkRepository {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    @Qualifier("getPostgresProperties")
    private Properties postgresProperties;

    public List<Source> getSource() {
        return sparkSession.read()
                .jdbc(postgresProperties.getProperty("url"), "sources", postgresProperties)
                .toDF()
                .as(Encoders.bean(Source.class))
                .collectAsList();
    }

    public void save(List<Source> sources) {
        Dataset<Source> datasetSource = sparkSession.createDataset(sources, Encoders.bean(Source.class));
        datasetSource
                .select("name")
                .write()
                .mode(SaveMode.Append)
                .jdbc(postgresProperties.getProperty("url"), "sources", postgresProperties);
    }

}
