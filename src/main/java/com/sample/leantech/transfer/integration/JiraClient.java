package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JiraClient {

    private static final String PROJECT_PATH_JIRA = "/rest/api/2/project";

    private static final String EPIC_PATH_JIRA = "/rest/api/2/search?jql=issuetype=\"Epic\"";
    private static final String NON_EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/none/issue";
    private static final String EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/{epicId}/issue";

    private static final String ISSUE_PATH_JIRA = "/rest/api/2/search?jql=project={projectName}";

    private static final String WORKLOG_PATH_JIRA = "/rest/api/2/issue/{issueId}/worklog";
    private static final String USER_PATH_JIRA = "/rest/api/2/user/search?username={username}";

    private final RestTemplate restTemplate;

    public List<JiraProjectDto> getProjects()  {
        return restTemplate.exchange(PROJECT_PATH_JIRA, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraProjectDto>>() {}).getBody();
    }

    @Async
    public CompletableFuture<List<JiraProjectDto>> getProjectsAsync() {
        return CompletableFuture.completedFuture(getProjects());
    }

    public JiraIssueResponseDto getEpics() {
        return restTemplate.exchange(EPIC_PATH_JIRA, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getEpicsAsync() {
        return CompletableFuture.completedFuture(getEpics());
    }

    public JiraIssueResponseDto getNonEpicIssues() {
        return restTemplate.exchange(NON_EPIC_ISSUE_PATH_JIRA, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getNonEpicIssuesAsync() {
        return CompletableFuture.completedFuture(getNonEpicIssues());
    }

    public JiraIssueResponseDto getEpicIssues(String epicId) {
        String url = UriComponentsBuilder.fromPath(EPIC_ISSUE_PATH_JIRA)
                .buildAndExpand(epicId)
                .toString();
        return restTemplate.exchange(url, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getEpicIssuesAsync(String epicId) {
        return CompletableFuture.completedFuture(getEpicIssues(epicId));
    }

    public List<JiraIssueDto> getIssues(String projectName) {
        String url = UriComponentsBuilder.fromPath(ISSUE_PATH_JIRA)
                .buildAndExpand(projectName)
                .toString();
        JiraIssueResponseDto responseDto = restTemplate.exchange(url, HttpMethod.GET, null,
                JiraIssueResponseDto.class).getBody();
        return responseDto == null ? Collections.emptyList() : responseDto.getIssues();
    }

    public JiraWorklogResponseDto getWorklogs(String issueId)  {
        String url = UriComponentsBuilder.fromPath(WORKLOG_PATH_JIRA)
                .buildAndExpand(issueId)
                .toString();
        return restTemplate.exchange(url, HttpMethod.GET, null, JiraWorklogResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraWorklogResponseDto> getWorklogsAsync(String issueId) {
        return CompletableFuture.completedFuture(getWorklogs(issueId));
    }

    public List<JiraUserDto> getUsers(String username) {
        return restTemplate.exchange(USER_PATH_JIRA, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraUserDto>>() {}, username).getBody();
    }

    @Async
    public CompletableFuture<List<JiraUserDto>> getUsersAsync(String username) {
        return CompletableFuture.completedFuture(getUsers(username));
    }

}