package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class IssueMapperTest {

    @Autowired
    private JavaSparkContext javaSparkCtx;

    @Test
    public void dtoToModeEpicIssue() {
        JiraIssueDto jiraIssueDto = issueEpicDto();
        TransferContext ctx = transferContext();
        Issue issue = IssueMapper.INSTANCE.dtoToModel(jiraIssueDto, ctx);
        Assertions.assertEquals(jiraIssueDto.getId(), String.valueOf(issue.getSourceId()));
        Assertions.assertEquals(jiraIssueDto.getKey(), issue.getName());
        Assertions.assertEquals(
                jiraIssueDto.getFields().getEpic().getId(),
                String.valueOf(issue.getHid())
        );
    }

    @Test
    public void dtoToModelParentIssue() {
        JiraIssueDto jiraIssueDto = issueParentDto();
        TransferContext ctx = transferContext();
        Issue issue = IssueMapper.INSTANCE.dtoToModel(jiraIssueDto, ctx);

        Assertions.assertEquals(jiraIssueDto.getId(), String.valueOf(issue.getSourceId()));
        Assertions.assertEquals(jiraIssueDto.getKey(), issue.getName());
        Assertions.assertEquals(
                jiraIssueDto.getFields().getProject().getId(),
                String.valueOf(issue.getPid())
        );
        Assertions.assertEquals(
                jiraIssueDto.getFields().getParent().getId(),
                String.valueOf(issue.getHid())
        );
    }

    @Test
    public void dtoToModelIssueWithoutEpicAndParent() {
        JiraIssueDto jiraIssueDto = issueDtoNoParentAndEpic();
        TransferContext ctx = transferContext();
        Issue issue = IssueMapper.INSTANCE.dtoToModel(jiraIssueDto, ctx);

        Assertions.assertEquals(jiraIssueDto.getId(), String.valueOf(issue.getSourceId()));
        Assertions.assertEquals(jiraIssueDto.getKey(), issue.getName());
        Assertions.assertEquals(
                jiraIssueDto.getFields().getProject().getId(),
                String.valueOf(issue.getPid())
        );
        Assertions.assertEquals(
                jiraIssueDto.getFields().getProject().getId(),
                String.valueOf(issue.getHid())
        );

    }

    @Test
    public void testRddStreamMapping(){
        JiraIssueDto jiraIssueDto = issueEpicDto();
        List<JiraIssueDto> listJiraProjectDto = Arrays.asList(jiraIssueDto);
        JavaRDD<JiraIssueDto> javaRddUsers = javaSparkCtx.parallelize(listJiraProjectDto);
        TransferContext ctx = transferContext();
        Collection<Issue> listProjectConvertedRdd = javaRddUsers.map(issue ->
                IssueMapper.INSTANCE.dtoToModel(issue, ctx)).collect();

        Assertions.assertNotNull(listProjectConvertedRdd);
        //Assertions.assertEquals(1, listProjectConvertedRdd.size());
    }

    private JiraIssueDto issueEpicDto(){
        JiraIssueDto jiraIssueDto = new JiraIssueDto();
            jiraIssueDto.setId("1400");
            jiraIssueDto.setKey("Key-123-test");
        JiraIssueDto.Fields fields = new JiraIssueDto.Fields();
            JiraIssueDto.Fields.Parent project = new JiraIssueDto.Fields.Parent();
                project.setId(String.valueOf(10000));
            JiraIssueDto.Fields.IssueType issueType = new JiraIssueDto.Fields.IssueType();
                issueType.setName("Test-Issue-Type");
            JiraIssueDto.Fields.Parent epic = new JiraIssueDto.Fields.Parent();
                epic.setId(String.valueOf(40001));
        fields.setProject(project);
        fields.setEpic(epic);
        fields.setIssuetype(issueType);
        fields.setSummary("Test-Summery");
        jiraIssueDto.setFields(fields);

        return jiraIssueDto;
    }

    private JiraIssueDto issueParentDto(){
        JiraIssueDto jiraIssueDto = new JiraIssueDto();
        jiraIssueDto.setId("1400");
        jiraIssueDto.setKey("Key-123-test");
        JiraIssueDto.Fields fields = new JiraIssueDto.Fields();
        JiraIssueDto.Fields.Parent project = new JiraIssueDto.Fields.Parent();
        project.setId(String.valueOf(10000));
        JiraIssueDto.Fields.Parent parent = new JiraIssueDto.Fields.Parent();
            parent.setId(String.valueOf(100015));
        JiraIssueDto.Fields.IssueType issueType = new JiraIssueDto.Fields.IssueType();
        issueType.setName("Test-Issue-Type");
        fields.setProject(project);
        fields.setParent(parent);
        fields.setIssuetype(issueType);
        fields.setSummary("Test-Summery");
        jiraIssueDto.setFields(fields);

        return jiraIssueDto;

    }

    private JiraIssueDto issueDtoNoParentAndEpic(){
        JiraIssueDto jiraIssueDto = new JiraIssueDto();
        jiraIssueDto.setId("1400");
        jiraIssueDto.setKey("Key-123-test");
        JiraIssueDto.Fields fields = new JiraIssueDto.Fields();
        JiraIssueDto.Fields.Parent project = new JiraIssueDto.Fields.Parent();
        project.setId(String.valueOf(10000));
        JiraIssueDto.Fields.IssueType issueType = new JiraIssueDto.Fields.IssueType();
        issueType.setName("Test-Issue-Type");
        fields.setProject(project);
        fields.setIssuetype(issueType);
        fields.setSummary("Test-Summery");
        jiraIssueDto.setFields(fields);
        return jiraIssueDto;
    }

    private TransferContext transferContext() {
        TransferContext ctx = new TransferContext();
        ctx.setSource(Source.JIRA_1);
        ctx.setLogId(1);
        return ctx;
    }

}