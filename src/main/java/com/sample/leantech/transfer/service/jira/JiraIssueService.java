package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.integration.JiraIssueClient;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraIssueService {

    private final JiraIssueClient jiraIssueClient;

    public List<JiraIssueDto> getIssues(String projectName) {
        return jiraIssueClient.getIssues(projectName);
    }

}