package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.Arrays;

@ActiveProfiles(value = {"tests", "default"})
public abstract class AbstractMapperTest {

    static final Integer LOG_ID = 1;
    static final String JIRA_USER_PREFIX = "JIRAUSER";
    static final String USER_ID = "10001";

    TransferContext transferContext() {
        TransferContext ctx = new JiraTransferContext(Source.JIRA);
        ctx.getLogInfo().setLogId(LOG_ID);
        return ctx;
    }

    JiraProjectDto jiraProjectDto(){
        JiraProjectDto dto = new JiraProjectDto();
        dto.setId("123");
        dto.setName("Test_jira-Project");
        return dto;
    }

    JiraIssueDto jiraIssueDto() {
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

        JiraIssueDto.Fields.Customfield customfield = new JiraIssueDto.Fields.Customfield();
        customfield.setId("10004");
        customfield.setValue("customfield_value_test");
        fields.setCustomfield(customfield);

        JiraIssueDto.Fields.Component component1 = new JiraIssueDto.Fields.Component();
        component1.setId("34");
        component1.setValue("Component1-test");
        JiraIssueDto.Fields.Component component2 = new JiraIssueDto.Fields.Component();
        component1.setId("44");
        component1.setValue("Component2-test");
        JiraIssueDto.Fields.Component component3 = new JiraIssueDto.Fields.Component();
        component1.setId("55");
        component1.setValue("Component3-test");
        fields.setComponents(Arrays.asList(component1, component2, component3));

        dto.setFields(fields);

        return dto;
    }

    JiraWorklogDto jiraWorklogDto() {
        JiraWorklogDto dto = new JiraWorklogDto();
        dto.setId("123");
        dto.setIssueId("444");
        dto.setTimeSpentSeconds(202L);
        dto.setUpdateAuthor(jiraUserDto());
        dto.setUpdated(ZonedDateTime.now());
        return dto;
    }

    JiraUserDto jiraUserDto() {
        JiraUserDto dto = new JiraUserDto();
        dto.setKey(JIRA_USER_PREFIX + USER_ID);
        dto.setName("Name-Test-1");
        return dto;
    }

}