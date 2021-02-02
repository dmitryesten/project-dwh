package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.Project;
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

@SpringBootTest
class ProjectMapperTest {

    @Autowired
    private JavaSparkContext javaSparkCtx;

    @Test
    public void test(){
        JiraProjectDto jiraProjectDtoTest = getJiraProjectDto();
        List<JiraProjectDto> listJiraProjectDto = Arrays.asList(jiraProjectDtoTest);
        JavaRDD<JiraProjectDto> javaRddUsers = javaSparkCtx.parallelize(listJiraProjectDto);

        Collection<Project> listProjectConvertedRdd = javaRddUsers.map(ProjectMapper.INSTANCE::dtoToModel).collect();

        Assertions.assertEquals(1, listProjectConvertedRdd.size());
        Assertions.assertEquals(jiraProjectDtoTest.getName(),
                listProjectConvertedRdd.stream()
                        .filter(s -> s.getName().equals(getJiraProjectDto().getName()))
                        .findFirst().get().getName());

    }

    private JiraProjectDto getJiraProjectDto(){
        JiraProjectDto dto = new JiraProjectDto();
        dto.setKey(UUID.randomUUID().toString());
        dto.setName("Test_jira-Project");
        dto.setProjectTypeKey("Prohect-Test");
        return dto;
    }

}