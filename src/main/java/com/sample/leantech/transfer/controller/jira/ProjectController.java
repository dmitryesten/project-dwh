package com.sample.leantech.transfer.controller.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;
    private final JiraProjectService jiraProjectService;

    @GetMapping("/project")
    public List<JiraProjectRequestDto> getProject() throws JsonProcessingException {
        List<JiraProjectRequestDto> listProject = objectMapper.readValue(jiraProjectService.getProject().getBody(), new TypeReference<List<JiraProjectRequestDto>>(){});
        return listProject;
    }

}