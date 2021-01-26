package com.sample.leantech.transfer.controller.jira;

import com.sample.leantech.transfer.model.dto.request.JiraProjectRequestDto;
import com.sample.leantech.transfer.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
public class ProjectController implements IJiraRequestMapping {

    private final JiraProjectService jiraProjectService;

    @GetMapping("/project")
    public List<JiraProjectRequestDto> getProjects() {
        List<JiraProjectRequestDto> projects = jiraProjectService.getProjects();
        return projects;
    }

}