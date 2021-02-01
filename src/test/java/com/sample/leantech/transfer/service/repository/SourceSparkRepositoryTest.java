package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class SourceSparkRepositoryTest {

    @Autowired
    private SourceSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of sourceRepository")
    public void testSaveGet() {
        Source sourceJiraTest = new Source();
            sourceJiraTest.setName(UUID.randomUUID().toString());
        Source sourceRedmine = new Source();
            sourceRedmine.setName(UUID.randomUUID().toString());

        List<Source> listNewSource = Arrays.asList(sourceJiraTest, sourceRedmine);

        repository.save(listNewSource);

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