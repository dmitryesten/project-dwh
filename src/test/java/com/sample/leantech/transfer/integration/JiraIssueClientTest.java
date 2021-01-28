package com.sample.leantech.transfer.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;

@SpringBootTest
class JiraIssueClientTest {

    @Autowired
    private JiraIssueClient jiraIssueClient;

    @Test
    public void testIssueNode() throws IOException {
        Assertions.assertNotNull(jiraIssueClient.getIssues("MTASK"));
    }

}