package com.sample.leantech.transfer.integration;

import com.sample.leantech.transfer.model.dto.request.JiraWorklogResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class JiraWorklogClient {

    private static final String WORKLOG_PATH_JIRA = "/rest/api/2/issue/{issueId}/worklog";

    private final RestTemplate restTemplate;

    public JiraWorklogResponseDto getWorklogs(String issueId)  {
        String url = UriComponentsBuilder.fromPath(WORKLOG_PATH_JIRA)
                .buildAndExpand(issueId)
                .toString();
        return restTemplate.exchange(url, HttpMethod.GET, null, JiraWorklogResponseDto.class).getBody();
    }

    @Async
    public CompletableFuture<JiraWorklogResponseDto> getWorklogsAsync(String issueId) {
        return CompletableFuture.completedFuture(getWorklogs(issueId));
    }

}