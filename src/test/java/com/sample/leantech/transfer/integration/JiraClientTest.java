package com.sample.leantech.transfer.integration;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AssertJProxySetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class JiraClientTest {

    @Autowired
    private JiraClient jiraClient;

    @Test
    @DisplayName("Testing getNonEpicIssues() that it's not null")
    void getNonEpicIssues() {
        Assertions.assertNotNull(jiraClient.getNonEpicIssues());
        Assertions.assertFalse(jiraClient.getNonEpicIssues().getIssues().isEmpty());
        jiraClient.getNonEpicIssues().getIssues().forEach(issueDto -> log.info(issueDto.toString()));
    }

    @Test
    @DisplayName("Testing getProjects() that it's not empty")
    void getProjects() {
        Assertions.assertNotNull(jiraClient.getProjects());
        Assertions.assertFalse(jiraClient.getProjects().isEmpty());
        jiraClient.getProjects().forEach(project -> log.info(project.toString()));
    }

}