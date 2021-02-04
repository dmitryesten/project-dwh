package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ProjectMapperTest {

    @Test
    public void test(){
        JiraProjectDto jiraProjectDtoTest = jiraProjectDto();
        List<JiraProjectDto> projects = Arrays.asList(jiraProjectDtoTest);
        TransferContext ctx = transferContext();
        List<Project> listProjectConverted = projects
                .stream()
                .map(project -> ProjectMapper.INSTANCE.dtoToModel(project, ctx))
                .collect(Collectors.toList());

        Assertions.assertEquals(1, listProjectConverted.size());
        Assertions.assertEquals(jiraProjectDtoTest.getName(),
                listProjectConverted.stream()
                        .filter(s -> s.getName().equals(jiraProjectDtoTest.getName()))
                        .findFirst().get().getName());

    }

    private JiraProjectDto jiraProjectDto(){
        JiraProjectDto dto = new JiraProjectDto();
        dto.setName("Test_jira-Project");
        dto.setId("123");
        return dto;
    }

    private TransferContext transferContext() {
        TransferContext ctx = new TransferContext();
        ctx.setSource(Source.JIRA_1);
        ctx.setLogId(1);
        return ctx;
    }

}