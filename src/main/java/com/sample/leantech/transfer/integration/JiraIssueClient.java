package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.JiraIssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JiraIssueClient {

    private static final String EPIC_PATH_JIRA = "/rest/api/2/search?jql=issuetype=\"Epic\"";
    private static final String NON_EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/none/issue";
    private static final String EPIC_ISSUE_PATH_JIRA = "/rest/agile/1.0/epic/{epicId}/issue";

    private final RestTemplate restTemplate;

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

    private static final String ISSUE_PATH_JIRA = "/rest/api/2/search?jql=project={project}";

    //private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public List<JiraIssueDto> getIssues(String projectName) throws IOException {
        String fullJsonIssues =
                restTemplate.exchange(ISSUE_PATH_JIRA, HttpMethod.GET, null,
                        String.class, projectName).getBody();
        JsonNode jsonIssuesNode = objectMapper.readTree(fullJsonIssues).get("issues");

        return getListIssues(jsonIssuesNode);

    }

    private List<JiraIssueDto> getListIssues(JsonNode jsonNodeArray) throws IOException {
        List<JiraIssueDto> listJiraIssue = new LinkedList<>();
        if (jsonNodeArray.isArray()) {
            for (JsonNode objNode : jsonNodeArray) {
                listJiraIssue.add(objectMapper.readValue(objectMapper.writeValueAsString(objNode), JiraIssueDto.class));
            }
        }
        return listJiraIssue;
    }

}
