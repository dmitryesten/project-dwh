package com.sample.leantech.transfer.controller.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.leantech.transfer.model.dto.request.JiraUserRequestDto;
import com.sample.leantech.transfer.service.jira.JiraUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements IJiraRequestMapping {

    private final JiraUserService jiraUserService;

    @GetMapping("/user")
    public List<JiraUserRequestDto> getUsers() throws JsonProcessingException {
        List<JiraUserRequestDto> users = jiraUserService.getUsers(".", "0", "1000");
        return users;
    }

}