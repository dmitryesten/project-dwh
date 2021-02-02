package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.dto.request.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TransferContext {

    private Long logId;
    private ZonedDateTime startDateTime;
    private List<JiraProjectDto> projects;
    private List<JiraIssueDto> epics;
    private List<JiraIssueDto> issues;
    private List<JiraWorklogDto> worklogs;
    private List<JiraUserDto> users;

}