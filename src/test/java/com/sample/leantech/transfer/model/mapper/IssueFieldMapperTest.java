package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.IssueField;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class IssueFieldMapperTest extends AbstractMapperTest{

    @Test
    public void test() throws NoSuchFieldException {
        JiraIssueDto issueDto = jiraIssueDto();
        issueDto.getFields().setParent(null);
        issueDto.getFields().setEpic(null);
        TransferContext ctx = transferContext();

        IssueField issue = IssueFieldMapper.INSTANCE.dtoToModel(issueDto, ctx);

        assertThat(issue.getId()).isNull();
        assertThat(issue.getSid()).isEqualTo(ctx.getSource().getValue());
        assertThat(issue.getLogId()).isEqualTo(ctx.getLogInfo().getLogId());
        assertThat(issue.getValue()).isEqualTo(issueDto.getFields().getCustomfield().getValue());
        assertThat(issue.getType()).isNotEmpty();
    }

}