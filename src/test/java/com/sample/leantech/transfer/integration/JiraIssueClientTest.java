package com.sample.leantech.transfer.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JiraIssueClientTest {

    @Autowired
    private JiraIssueClient jiraIssueClient;

    @Test
    public void testIssueNode() throws JsonProcessingException {
        System.out.println("Ответ: "+ jiraIssueClient.getIssues("Test-Project-1"));
    }

}