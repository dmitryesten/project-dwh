package com.sample.leantech.transfer.model.context;

import lombok.Data;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransferContext {

    private final Integer logId;
    private final ZonedDateTime startDateTime;
    private final List<JiraResult> jiraResults;
    private final DatabaseModel databaseModel;

    public TransferContext(Integer logId) {
        this.logId = logId;
        this.startDateTime = ZonedDateTime.now(Clock.systemUTC());
        this.jiraResults = new ArrayList<>();
        this.databaseModel = new DatabaseModel(logId);
    }

    public JiraResult addJiraResult(Source source) {
        JiraResult jiraResult = new JiraResult(logId, source);
        jiraResults.add(jiraResult);
        return jiraResult;
    }

}