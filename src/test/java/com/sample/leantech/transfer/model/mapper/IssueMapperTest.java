package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IssueMapperTest extends AbstractMapperTest {

    @Test
    public void testDtoToModelEpic() {
        JiraIssueDto issueDto = jiraIssueDto();
        issueDto.getFields().setParent(null);
        issueDto.getFields().setEpic(null);
        TransferContext ctx = transferContext();

        Issue issue = IssueMapper.INSTANCE.dtoToModel(issueDto, ctx);

        assertThat(issue.getId()).isNull();
        assertThat(issue.getPid()).isEqualTo(Integer.valueOf(issueDto.getFields().getProject().getId()));
        assertThat(issue.getSid()).isEqualTo(ctx.getSource().getValue());
        assertThat(issue.getLogId()).isEqualTo(ctx.getLogInfo().getLogId());
        assertThat(issue.getHid()).isNull();
        assertThat(issue.getSourceId()).isEqualTo(Integer.valueOf(issueDto.getId()));
        assertThat(issue.getType()).isEqualTo(issueDto.getFields().getIssuetype().getName());
        assertThat(issue.getName()).isEqualTo(issueDto.getKey());
        assertThat(issue.getSummery()).isEqualTo(issueDto.getFields().getSummary());
        assertThat(issue.getCreateDt()).isNull();
    }

    @Test
    public void testDtoToModelEpicIssue() {
        JiraIssueDto issueDto = jiraIssueDto();
        issueDto.getFields().setParent(null);
        TransferContext ctx = transferContext();

        Issue issue = IssueMapper.INSTANCE.dtoToModel(issueDto, ctx);

        assertThat(issue.getHid()).isEqualTo(Integer.valueOf(issueDto.getFields().getEpic().getId()));
    }

    @Test
    public void testDtoToModelNonEpicIssue() {
        JiraIssueDto issueDto = jiraIssueDto();
        issueDto.getFields().setEpic(null);
        TransferContext ctx = transferContext();

        Issue issue = IssueMapper.INSTANCE.dtoToModel(issueDto, ctx);

        assertThat(issue.getHid()).isEqualTo(Integer.valueOf(issueDto.getFields().getParent().getId()));
    }

    private JiraIssueDto jiraIssueDto() {
        JiraIssueDto dto = new JiraIssueDto();
        dto.setId("1400");
        dto.setKey("Key-123-test");

        JiraIssueDto.Fields fields = new JiraIssueDto.Fields();
        fields.setSummary("Test-Summery");

        JiraIssueDto.Fields.IssueType issueType = new JiraIssueDto.Fields.IssueType();
        issueType.setName("Test-Issue-Type");
        fields.setIssuetype(issueType);

        JiraIssueDto.Fields.Parent project = new JiraIssueDto.Fields.Parent();
        project.setId("10001");
        fields.setProject(project);

        JiraIssueDto.Fields.Parent epic = new JiraIssueDto.Fields.Parent();
        epic.setId("10002");
        fields.setEpic(epic);

        JiraIssueDto.Fields.Parent parent = new JiraIssueDto.Fields.Parent();
        parent.setId("10003");
        fields.setParent(parent);

        dto.setFields(fields);

        return dto;
    }

}