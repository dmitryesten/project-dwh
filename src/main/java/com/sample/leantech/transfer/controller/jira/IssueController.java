package com.sample.leantech.transfer.controller.jira;

import com.sample.leantech.transfer.integration.JiraClient;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
public class IssueController {

    private final JiraClient jiraClient;

    @GetMapping("/issue")
    public List<JiraIssueDto> getIssues(@RequestParam(defaultValue = ".") String projectName) {
        return jiraClient.getIssues(projectName);
    }

}