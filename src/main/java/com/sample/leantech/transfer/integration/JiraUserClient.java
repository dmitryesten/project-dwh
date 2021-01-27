package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class JiraUserClient {

    private static final String USER_PATH_JIRA = "/rest/api/2/user/search?username={username}";

    private final RestTemplate restTemplate;

    public List<JiraUserDto> getUsers(String username) {
        return restTemplate.exchange(USER_PATH_JIRA, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<JiraUserDto>>() {}, username).getBody();
    }

    @Async
    public CompletableFuture<List<JiraUserDto>> getUsersAsync(String username) {
        return CompletableFuture.completedFuture(getUsers(username));
    }

}