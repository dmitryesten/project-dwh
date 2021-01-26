package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.dto.request.JiraProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private static final String PROJECT_PATH_JIRA = "/rest/api/2/project";

    private final RestTemplate restTemplate;

    public List<JiraProjectRequestDto> getProjects() {
        return restTemplate.exchange(PROJECT_PATH_JIRA, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraProjectRequestDto>>() {}).getBody();
    }

}