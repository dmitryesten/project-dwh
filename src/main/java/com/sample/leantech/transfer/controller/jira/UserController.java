package com.sample.leantech.transfer.controller.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.service.jira.JiraUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jira")
@RequiredArgsConstructor
public class UserController {

    private final JiraUserService jiraUserService;

    @GetMapping("/user")
    public List<JiraUserDto> getUsers(@RequestParam(defaultValue = ".") String username) {
        List<JiraUserDto> users = jiraUserService.getUsers(username);
        return users;
    }

}