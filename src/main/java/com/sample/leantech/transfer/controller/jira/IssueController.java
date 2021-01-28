package com.sample.leantech.transfer.controller.jira;

import com.sample.leantech.transfer.integration.JiraIssueClient;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.service.jira.JiraIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
public class IssueController {

    private final JiraIssueClient jiraIssueService;

    @GetMapping("/issue")
    public List<JiraIssueDto> getProjects(@RequestParam(defaultValue = ".") String projectName) throws IOException {
        return jiraIssueService.getIssues(projectName);
    }

}
