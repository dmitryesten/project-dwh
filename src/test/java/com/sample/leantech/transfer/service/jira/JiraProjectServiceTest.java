package com.sample.leantech.transfer.service.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("tests,default")
class JiraProjectServiceTest {

    @Autowired
    private JiraProjectService jiraProjectService;

    @Test
    public void getProjectTest() throws JsonProcessingException {
        System.out.println(jiraProjectService.getProjects());
        Assertions.assertNotNull(jiraProjectService.getProjects());
    }

}