package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.integration.JiraProjectClient;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private final JiraProjectClient jiraProjectClient;

    public List<JiraProjectDto> getProjects()  {
        return jiraProjectClient.getProjects();
    }

}