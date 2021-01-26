package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraUserService {

    private static final String USER_PATH_JIRA = "/rest/api/2/user/search?username={username}&startAt={startAt}&maxResults={maxResults}";

    private final RestTemplate restTemplate;

    public List<JiraUserDto> getUsers(String username, String startAt, String maxResults) {
        String url = UriComponentsBuilder.fromPath(USER_PATH_JIRA)
                .build(username, startAt, maxResults)
                .toString();
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraUserDto>>() {}).getBody();
    }

}