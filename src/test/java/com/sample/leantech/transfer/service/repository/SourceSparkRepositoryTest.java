package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Source;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.expressions.Sequence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
class SourceSparkRepositoryTest {
    @Autowired
    private SparkSession sparkSession;
    @Autowired
    private SourceSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of sourceRepository")
    public void testSaveGet() {
        Source sourceJiraTest = new Source();
            sourceJiraTest.setName(com.sample.leantech.transfer.model.context.Source.JIRA_1.name());
        Source sourceRedmine = new Source();
            sourceRedmine.setName("Redmine_2");

        List<Source> sourcesOfJira = Arrays.asList(sourceJiraTest, sourceRedmine);
        Dataset<Source> datasetOfJiraRow = sparkSession.createDataset(sourcesOfJira, Encoders.bean(Source.class));
            datasetOfJiraRow.show();


        repository.save(sourcesOfJira);

        Assertions.assertNotNull(repository.get());

        Assertions.assertEquals(sourceJiraTest.getName(),
                repository.get().stream()
                        .map(Source.class::cast)
                        .filter(objectSource -> objectSource.getName().equals(sourceJiraTest.getName()))
                        .findFirst().get().getName(), "Name's name jira values is not equals\"");

        Assertions.assertEquals(sourceRedmine.getName(),
                repository.get().stream()
                        .map(Source.class::cast)
                        .filter(objectSource -> objectSource.getName().equals(sourceRedmine.getName()))
                        .findFirst().get().getName(), "Name's name redmine name values is not equals");



    }

}