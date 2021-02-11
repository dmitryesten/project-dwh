package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Worklog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
class WorklogSparkRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private WorklogSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of WorklogRepository")
    public void testSaveGet() {
        Worklog worklog = new Worklog();
            worklog.setIssueId(null);
            worklog.setLogId(null);
            worklog.setSid(null);
            worklog.setIssueId(null);
            worklog.setUpdated(new Timestamp(System.currentTimeMillis()));
            worklog.setTimeSpentSecond(new Random().nextInt());
            worklog.setUsername(UUID.randomUUID().toString());
            worklog.setUserId(null);
        List<Worklog> worklogList = Arrays.asList(worklog);

        repository.save(worklogList);

        Assertions.assertFalse(repository.get().isEmpty());

        Assertions.assertEquals(worklog.getTimeSpentSecond(),
                repository.get().stream()
                        .map(Worklog.class::cast)
                        .filter(objectWorklog -> objectWorklog.getTimeSpentSecond().equals(worklog.getTimeSpentSecond()))
                        .findFirst().get().getTimeSpentSecond(),
                "TimeSpentSecond's worklogs is not equals"
        );
        Assertions.assertEquals(worklog.getUpdated(),
                repository.get().stream()
                        .map(Worklog.class::cast)
                        .filter(objectWorklog -> objectWorklog.getUpdated().equals(worklog.getUpdated()))
                        .findFirst().get().getUpdated(),
                "UpdateTime's worklogs is not equals"
        );

    }

    @Test
    public void testGroupedTest(){
        repository.getGroupedWorklogMaxLogIdBySourceId().show();
        Assertions.assertNotNull(repository.getGroupedWorklogMaxLogIdBySourceId());
    }

    @Test
    public void testGetWorklogWithMaxLogIdBySourceId() {
        repository.getWorklogWithMaxLogIdBySourceId().show();
        Assertions.assertNotNull(repository.getWorklogWithMaxLogIdBySourceId());
    }

}