package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Component("jiraClient")
public class JiraClient {

    private static final String PROJECT_PATH_JIRA = "/rest/api/2/project";

    private static final String EPIC_PATH_JIRA = "/rest/api/2/search?jql=issuetype=\"Epic\"";
    private static final String NON_EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/none/issue";
    private static final String EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/{epicId}/issue";

    private static final String WORKLOG_PATH_JIRA = "/rest/api/2/issue/{issueId}/worklog";

    private final RestTemplate jiraRestTemplate;

    public JiraClient(@Qualifier("jiraRestTemplate") RestTemplate jiraRestTemplate) {
        this.jiraRestTemplate = jiraRestTemplate;
    }

    public List<JiraProjectDto> getProjects()  {
        return jiraRestTemplate.exchange(PROJECT_PATH_JIRA, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraProjectDto>>() {}).getBody();
    }

    @Async
    public CompletableFuture<List<JiraProjectDto>> getProjectsAsync() {
        return CompletableFuture.completedFuture(getProjects());
    }

    public JiraIssueResponseDto getEpics() {
        return jiraRestTemplate.exchange(EPIC_PATH_JIRA, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getEpicsAsync() {
        return CompletableFuture.completedFuture(getEpics());
    }

    public JiraIssueResponseDto getNonEpicIssues() {
        return jiraRestTemplate.exchange(NON_EPIC_ISSUE_PATH_JIRA, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getNonEpicIssuesAsync() {
        return CompletableFuture.completedFuture(getNonEpicIssues());
    }

    public JiraIssueResponseDto getEpicIssues(String epicId) {
        String url = UriComponentsBuilder.fromPath(EPIC_ISSUE_PATH_JIRA)
                .buildAndExpand(epicId)
                .toString();
        return jiraRestTemplate.exchange(url, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getEpicIssuesAsync(String epicId) {
        return CompletableFuture.completedFuture(getEpicIssues(epicId));
    }

    public JiraWorklogResponseDto getWorklogs(String issueId)  {
        String url = UriComponentsBuilder.fromPath(WORKLOG_PATH_JIRA)
                .buildAndExpand(issueId)
                .toString();
        return jiraRestTemplate.exchange(url, HttpMethod.GET, null, JiraWorklogResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraWorklogResponseDto> getWorklogsAsync(String issueId) {
        return CompletableFuture.completedFuture(getWorklogs(issueId));
    }

}