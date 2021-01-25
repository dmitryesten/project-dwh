package com.sample.leantech.transfer.controller.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.leantech.transfer.model.dto.request.JiraProjectRequestDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserRequestDto;
import com.sample.leantech.transfer.service.jira.JiraUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements IJiraRequestMapping {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JiraUserService jiraUserService;

    @GetMapping("/user")
    public List<JiraUserRequestDto>  getUser() throws JsonProcessingException {
        List<JiraUserRequestDto> listUser =
                objectMapper.readValue(jiraUserService.getUser().getBody(), new TypeReference<List<JiraUserRequestDto>>(){});
        return listUser;
    }

}
