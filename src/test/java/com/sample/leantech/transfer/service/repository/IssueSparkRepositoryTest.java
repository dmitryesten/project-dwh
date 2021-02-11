package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Issue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class IssueSparkRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private IssueSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of issueRepository")
    public void testSaveGet() {
        Issue issue = new Issue();
            issue.setPid(null);
            issue.setSid(null);
            issue.setLogId(null);
            issue.setHid(null);
            issue.setSourceId(new Random().nextInt());
            issue.setType("Test-Sample-Task");
            issue.setName(UUID.randomUUID().toString());
            issue.setSummery("This is test summary about own and type Test-Sample-Task");
        List<Issue> listIssue = Arrays.asList(issue);

        repository.save(listIssue);

        Assertions.assertNotNull(repository.get());
        Assertions.assertEquals(issue.getName(),
                repository.get().stream()
                        .map(Issue.class::cast)
                        .filter(objectIssue -> objectIssue.getName().equals(issue.getName()))
                        .findFirst().get().getName(), "Name's issue name values is not equals");

    }

    @Test
    public void testGroupedIssues() {
        repository.getGroupedIssueMaxLogIdBySourceId().show();
        Assertions.assertNotNull(repository.getGroupedIssueMaxLogIdBySourceId());
    }

    @Test
    public void testJoinIssueAndGroupedIssue() {
        repository.getIssueWithMaxLogIdBySourceId().show();
        Assertions.assertNotNull(repository.getIssueWithMaxLogIdBySourceId());
    }

}