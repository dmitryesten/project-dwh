package com.sample.leantech.transfer.integration;

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

import java.io.DataInput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class JiraIssueClient {

    private static final String ISSUE_PATH_JIRA = "/rest/api/2/search?jql=project={project}";

    private final RestTemplate restTemplate;

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
