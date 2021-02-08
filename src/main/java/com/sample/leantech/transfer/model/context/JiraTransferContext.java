package com.sample.leantech.transfer.model.context;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class JiraTransferContext extends TransferContext {

    @Getter
    private final List<JiraResult> jiraResults;

    public JiraTransferContext(Integer logId) {
        super(logId);
        this.jiraResults = new ArrayList<>();
    }

    public JiraResult addJiraResult(Source source) {
        JiraResult jiraResult = new JiraResult(logId, source);
        jiraResults.add(jiraResult);
        return jiraResult;
    }

}