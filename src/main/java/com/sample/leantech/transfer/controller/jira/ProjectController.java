package com.sample.leantech.transfer.controller.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
public class ProjectController {

    @Autowired
    private final JiraProjectService jiraProjectService;

    @Autowired
    private final SparkSession sparkSession;

    @GetMapping("/project")
    public List<JiraProjectDto> getProjects() throws JsonProcessingException {
        Dataset<JiraProjectDto> dataSet = sparkSession.createDataset(jiraProjectService.getProjects(), Encoders.bean(JiraProjectDto.class));
        dataSet.show();
        List<JiraProjectDto> projects = jiraProjectService.getProjects();
        return projects;
    }

}