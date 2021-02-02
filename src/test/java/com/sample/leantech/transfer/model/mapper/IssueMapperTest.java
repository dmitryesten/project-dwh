package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueMapperTest {

    @Autowired
    private JavaSparkContext javaSparkCtx;

    @Test
    public void test(){
        JiraIssueDto jiraIssueDto = getIssueDto();
        List<JiraIssueDto> listJiraProjectDto = Arrays.asList(jiraIssueDto);

        //JavaRDD<JiraIssueDto> javaRddUsers = javaSparkCtx.parallelize(listJiraProjectDto);
        //Collection<Issue> listProjectConvertedRdd = javaRddUsers.map(IssueMapper.INSTANCE::dtoToModel).collect();

        Issue issue = IssueMapper.INSTANCE.dtoToModel(getIssueDto());
        //Assertions.assertNotNull(listProjectConvertedRdd);
        //Assertions.assertEquals(1, listProjectConvertedRdd.size());


    }

    private JiraIssueDto getIssueDto(){
        JiraIssueDto jiraIssueDto = new JiraIssueDto();
        JiraIssueDto.Fields fields = new JiraIssueDto.Fields();
        JiraIssueDto.Fields.Project project = new JiraIssueDto.Fields.Project();
            project.setId("12-R");
        fields.setProject(project);

        project.setId("12-sd");
            jiraIssueDto.setFields(fields);
            jiraIssueDto.setId("34");
            jiraIssueDto.setKey("Key-123-test");
        return jiraIssueDto;
    }

    private JiraProjectDto getJiraProjectDto(){
        JiraProjectDto dto = new JiraProjectDto();
        dto.setKey(UUID.randomUUID().toString());
        dto.setName("Test_jira-Project");
        dto.setProjectTypeKey("Prohect-Test");
        return dto;
    }

}