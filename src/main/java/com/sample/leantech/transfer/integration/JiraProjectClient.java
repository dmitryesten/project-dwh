package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class JiraProjectClient {

    private static final String PROJECT_PATH_JIRA = "/rest/api/2/project";

    private final RestTemplate restTemplate;

    public List<JiraProjectDto> getProjects()  {
        return restTemplate.exchange(PROJECT_PATH_JIRA, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraProjectDto>>() {}).getBody();
    }

    @Async
    public CompletableFuture<List<JiraProjectDto>> getProjectsAsync() {
        return CompletableFuture.completedFuture(getProjects());
    }

}