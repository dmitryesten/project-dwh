package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.integration.JiraClient;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private final JiraClient jiraClient;

    public List<JiraProjectDto> getProjects()  {
        return jiraClient.getProjects();
    }

}