package com.sample.leantech.transfer.service.jira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JiraUserService {

    private static final String FILE_PATH_JIRA = "/rest/api/2/user/search?username={username}&startAt={startAt}&maxResults={maxResults}";

    private RestTemplate restTemplate;

    @Autowired
    public JiraUserService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getUser(){
        return restTemplate.getForEntity(FILE_PATH_JIRA, String.class, ".", "0", "1000");
    }

}
