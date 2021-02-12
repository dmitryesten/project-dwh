package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@SpringBootTest
class WorklogMapperTest extends AbstractMapperTest {

    @Test
    public void test(){
        JiraWorklogDto jiraWorklogDto = worklogDto();
        TransferContext ctx = transferContext();
        Worklog worklog = WorklogMapper.INSTANCE.dtoToModel(jiraWorklogDto, ctx);
        Assertions.assertNotNull(worklog);
        Assertions.assertEquals(jiraWorklogDto.getIssueId(), String.valueOf(worklog.getIssueId()) );
        Assertions.assertEquals(jiraWorklogDto.getUpdated().toInstant(), worklog.getUpdated().toInstant());
    }

    @Test
    public void testMappingJira() {
        JiraWorklogDto jiraWorklogDto = worklogDto();
        TransferContext ctx = transferContext();
        Worklog worklog = WorklogMapper.INSTANCE.dtoToModel(jiraWorklogDto, ctx);

        Assertions.assertNotNull(worklog);
        //Assertions.assertEquals(jiraWorklogDto.getUpdateAuthor().getKey(), worklog.getUserKey());
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
        jiraUserDtoFirst.setKey("Key-Test-1");
        jiraUserDtoFirst.setName("Name-Test-1");
        return jiraUserDtoFirst;
    }

}