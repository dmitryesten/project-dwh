package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.JiraIssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JiraIssueClient {

    private static final String EPIC_PATH_JIRA = "/rest/api/2/search?jql=project={projectName} AND issuetype=\"Epic\"";
    private static final String NON_EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/none/issue";
    private static final String EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/{epicId}/issue";

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

    private static final String ISSUE_PATH_JIRA = "/rest/api/2/search?jql=project={project}";

    //private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public String getIssues(String projectName) throws JsonProcessingException {
        String subJsonIssues =
                restTemplate.exchange(ISSUE_PATH_JIRA, HttpMethod.GET, null,
                        String.class, projectName).getBody();
        System.out.println("subJsonIssues:" + subJsonIssues);
        log.info("subJsonIssues:" + subJsonIssues);
        JsonNode jsonNode = objectMapper.valueToTree(subJsonIssues);
        ArrayNode arrayNode = (ArrayNode) jsonNode.get("issues");
        return  jsonNode.get("issues").textValue();

    }

}
