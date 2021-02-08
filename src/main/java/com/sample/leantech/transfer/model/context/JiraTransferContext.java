package com.sample.leantech.transfer.model.context;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class JiraTransferContext extends TransferContext {

    @Getter
    private final List<JiraResult> jiraResults;

    public JiraTransferContext(Integer logId, Source source) {
        super(logId, source);
        this.jiraResults = new ArrayList<>();
    }

    public JiraResult addJiraResult(Source source) {
        JiraResult jiraResult = new JiraResult(logId, source);
        jiraResults.add(jiraResult);
        return jiraResult;
    }

}