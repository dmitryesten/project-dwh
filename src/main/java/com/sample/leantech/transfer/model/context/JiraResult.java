package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class JiraResult implements Serializable {

    private TransferContext ctx;
    private Source source;
    private List<JiraProjectDto> projects;
    private List<JiraIssueDto> epics;
    private List<JiraIssueDto> issues;
    private List<JiraWorklogDto> worklogs;
    private List<JiraUserDto> users;

}