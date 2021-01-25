package com.sample.leantech.transfer.service.jira;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class JiraProjectServiceTest {

    @Autowired
    private JiraProjectService jiraProjectService;

    @Test
    public void getProjectTest(){
        System.out.println(jiraProjectService.getProject());
        System.out.println("HEADER:" + jiraProjectService.getProject().getHeaders());
        System.out.println("BODY:" + jiraProjectService.getProject().getBody());
        System.out.println("STATUS"+ jiraProjectService.getProject().getStatusCode());
        Assertions.assertNotNull(jiraProjectService.getProject());
    }

}