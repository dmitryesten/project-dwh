package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WorklogMapperTest extends AbstractMapperTest {

    @Test
    public void testDtoToModel() {
        JiraWorklogDto worklogDto = jiraWorklogDto();
        TransferContext ctx = transferContext();

        Worklog worklog = WorklogMapper.INSTANCE.dtoToModel(worklogDto, ctx);

        assertThat(worklog.getId()).isNull();
        assertThat(worklog.getIssueId()).isEqualTo(Integer.valueOf(worklogDto.getIssueId()));
        assertThat(worklog.getLogId()).isEqualTo(ctx.getLogInfo().getLogId());
        assertThat(worklog.getSid()).isEqualTo(ctx.getSource().getValue());
        assertThat(worklog.getSourceId()).isEqualTo(Integer.valueOf(worklogDto.getId()));
        assertThat(worklog.getUpdated()).isEqualTo(Timestamp.valueOf(worklogDto.getUpdated().toLocalDateTime()));
        assertThat(worklog.getTimeSpentSecond()).isEqualTo(worklogDto.getTimeSpentSeconds().intValue());
        assertThat(worklog.getUsername()).isEqualTo(worklogDto.getUpdateAuthor().getName());
        assertThat(worklog.getUserId()).isEqualTo(Integer.valueOf(USER_ID));
    }

}