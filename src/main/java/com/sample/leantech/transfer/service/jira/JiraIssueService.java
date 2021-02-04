package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.integration.JiraClient;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraIssueService {

    private final JiraClient jiraClient;

    public List<JiraIssueDto> getIssues(String projectName) {
        return jiraClient.getIssues(projectName);
    }

}