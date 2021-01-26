package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JiraUserService {

    private static final String USER_PATH_JIRA =
            "/rest/api/2/user/search?username={username}";

    private final RestTemplate restTemplate;

    public List<JiraUserDto> getUsers(String username) {
        return restTemplate.exchange(USER_PATH_JIRA, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraUserDto>>() {}, username).getBody();
    }

}