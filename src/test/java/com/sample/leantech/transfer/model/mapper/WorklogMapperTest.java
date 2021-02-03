package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@SpringBootTest
class WorklogMapperTest {

    @Autowired
    private JavaSparkContext javaSparkCtx;

    @Test
    public void test(){
        JiraWorklogDto jiraWorklogDto = worklogDto();
        TransferContext ctx = transferContext();
        Worklog worklog = WorklogMapper.INSTANCE.dtoToModel(jiraWorklogDto, ctx);
        Assertions.assertNotNull(worklog);
        Assertions.assertEquals(jiraWorklogDto.getIssueId(), String.valueOf(worklog.getIssueId()) );
        Assertions.assertEquals(jiraWorklogDto.getUpdated().toInstant(), worklog.getUpdated().toInstant());
    }

    private JiraWorklogDto worklogDto(){
        JiraWorklogDto jiraWorklogDto = new JiraWorklogDto();
        jiraWorklogDto.setId("123");
        jiraWorklogDto.setIssueId("444");
        jiraWorklogDto.setTimeSpentSeconds(202L);
        jiraWorklogDto.setUpdateAuthor(userDtoFirst());
        jiraWorklogDto.setUpdated(ZonedDateTime.now());
        Timestamp timestamp = Timestamp.valueOf(jiraWorklogDto.getUpdated().toLocalDateTime());
        return jiraWorklogDto;
    }

    private JiraUserDto userDtoFirst(){
        JiraUserDto jiraUserDtoFirst = new JiraUserDto();
        jiraUserDtoFirst.setSelf("Test-self_test-1");
        jiraUserDtoFirst.setKey("Key-Test-1");
        jiraUserDtoFirst.setName("Name-Test-1");
        jiraUserDtoFirst.setEmailAddress("test1@mail.ru");
        jiraUserDtoFirst.setDisplayName("DisplayName");
        jiraUserDtoFirst.setActive(true);
        jiraUserDtoFirst.setDeleted(false);
        jiraUserDtoFirst.setTimeZone("6+");
        jiraUserDtoFirst.setLocale("RU");
        return jiraUserDtoFirst;
    }

    private TransferContext transferContext() {
        TransferContext ctx = new TransferContext();
        ctx.setSource(Source.JIRA_1);
        ctx.setLogId(1);
        return ctx;
    }

}