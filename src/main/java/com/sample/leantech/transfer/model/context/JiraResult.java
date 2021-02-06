package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import lombok.Data;

import java.util.List;

@Data
public class JiraResult {

    private final Integer parentLogId;
    private final Source source;
    private List<JiraProjectDto> projects;
    private List<JiraIssueDto> epics;
    private List<JiraIssueDto> issues;
    private List<JiraWorklogDto> worklogs;
    private List<JiraUserDto> users;

    JiraResult(Integer parentLogId, Source source) {
        this.parentLogId = parentLogId;
        this.source = source;
    }

}