package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.JiraIssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class JiraIssueClient {

    private static final String EPIC_PATH_JIRA = "/rest/api/2/search?jql=project={projectName} AND issuetype=\"Epic\"";
    private static final String NON_EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/none/issue";

    private final RestTemplate restTemplate;

    public JiraIssueResponseDto getEpics(String projectName) {
        String url = UriComponentsBuilder.fromPath(EPIC_PATH_JIRA)
                .buildAndExpand(projectName)
                .toString();
        return restTemplate.exchange(url, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getEpicsAsync(String projectName) {
        return CompletableFuture.completedFuture(getEpics(projectName));
    }

    public JiraIssueResponseDto getNonEpicIssues() {
        return restTemplate.exchange(NON_EPIC_ISSUE_PATH_JIRA, HttpMethod.GET, null, JiraIssueResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraIssueResponseDto> getNonEpicIssuesAsync() {
        return CompletableFuture.completedFuture(getNonEpicIssues());
    }

}