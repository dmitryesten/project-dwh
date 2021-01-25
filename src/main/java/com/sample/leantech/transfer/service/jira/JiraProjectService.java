package com.sample.leantech.transfer.service.jira;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JiraProjectService {

    private static final String FILE_PATH_JIRA = "/rest/api/2/project";

    private RestTemplate restTemplate;

    @Autowired
    public JiraProjectService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getProject(){
        return restTemplate.getForEntity(FILE_PATH_JIRA, String.class);
    }

}
