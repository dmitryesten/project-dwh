package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.JiraEpicResponseDto;
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

    private final RestTemplate restTemplate;

    public JiraEpicResponseDto getEpics(String projectName) {
        String url = UriComponentsBuilder.fromPath(EPIC_PATH_JIRA)
                .buildAndExpand(projectName)
                .toString();
        return restTemplate.exchange(url, HttpMethod.GET, null, JiraEpicResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraEpicResponseDto> getEpicsAsync(String projectName) {
        return CompletableFuture.completedFuture(getEpics(projectName));
    }

}