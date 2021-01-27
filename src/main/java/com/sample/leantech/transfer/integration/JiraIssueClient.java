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

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JiraIssueClient {

    private static final String ISSUE_PATH_JIRA = "/rest/api/2/search?jql=project={project}";

    private final RestTemplate restTemplate;

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
