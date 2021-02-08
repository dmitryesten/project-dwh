package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.LogTransfer;
import com.sample.leantech.transfer.model.db.Source;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
class LogTransferSparkRepositoryTest {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private SourceSparkRepository sourceRepository;

    @Autowired
    private LogTransferSparkRepository logTransferSparkRepository;

    @Test
    public void test() {
        Assertions.assertNotNull(logTransferSparkRepository.get());
        Dataset<Row> datasetLogs = logTransferSparkRepository.getDataset().toDF();
        Dataset<Row> datasetFilteredResult =
                datasetLogs.select("id", "sid", "startDt", "endDt", "result")
                .where(datasetLogs.col("endDt").isNull().and(datasetLogs.col("result").isNull()));

        datasetFilteredResult.show();
    }


    @Test
    public void testSave() {
        logTransferSparkRepository.save(Arrays.asList(getTransfer()));
        logTransferSparkRepository.save(Arrays.asList(getTransferFullRow()));
        Assertions.assertNotNull(logTransferSparkRepository.get());
    }

    private LogTransfer getTransfer() {
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.setSid(getIdCreatedTestSource());
        logTransfer.setStartDt(Timestamp.from(Instant.now()));
        return logTransfer;
    }

    private LogTransfer getTransferFullRow() {
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.setSid(getIdCreatedTestSource());
        logTransfer.setStartDt(Timestamp.from(Instant.now()));
        logTransfer.setEndDt(Timestamp.from(Instant.now()));
        logTransfer.setResult(true);
        return logTransfer;
    }

    private Integer getIdCreatedTestSource() {
        Source source = new Source();
            source.setName("Test-Jira-0");
        Collection<Source> sourceCollection = Arrays.asList(source);
        sourceRepository.save(sourceCollection);

        return sourceRepository.get().stream()
                .map(Source.class::cast)
                .filter(s -> s.getName().equals(source.getName()))
                .findFirst().get().getId();
    }

}