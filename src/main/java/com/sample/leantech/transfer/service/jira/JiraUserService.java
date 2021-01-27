package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.integration.JiraUserClient;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraUserService {

    private JiraUserClient jiraUserClient;

    public List<JiraUserDto> getUsers(String username) {
        return jiraUserClient.getUsers(username);
    }

}