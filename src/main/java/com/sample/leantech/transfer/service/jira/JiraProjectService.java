package com.sample.leantech.transfer.service.jira;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private static final String FILE_PATH_JIRA = "/rest/api/2/project";

    private final RestTemplate restTemplate;

    public ResponseEntity<String> getProject(){
        return restTemplate.getForEntity(FILE_PATH_JIRA, String.class);
    }

}