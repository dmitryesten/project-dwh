package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JiraResult {

    private List<JiraProjectDto> projects;
    private List<JiraIssueDto> epics;
    private List<JiraIssueDto> issues;
    private List<JiraWorklogDto> worklogs;
    private List<JiraUserDto> users;

    public JiraResult() {
        this.projects = new ArrayList<>();
        this.epics = new ArrayList<>();
        this.issues = new ArrayList<>();
        this.worklogs = new ArrayList<>();
        this.users = new ArrayList<>();
    }

}