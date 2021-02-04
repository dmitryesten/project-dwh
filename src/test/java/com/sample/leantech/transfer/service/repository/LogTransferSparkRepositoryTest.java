package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.LogTransfer;
import com.sample.leantech.transfer.model.db.Source;
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
    }

    @Test
    public void testSave() {
        logTransferSparkRepository.save(Arrays.asList(getTransfer()));

    }

    private LogTransfer getTransfer() {
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.setSid(getIdCreatedTestSource());
        logTransfer.setStartDt(Timestamp.from(Instant.now()));
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