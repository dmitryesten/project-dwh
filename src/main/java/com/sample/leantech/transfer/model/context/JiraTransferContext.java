package com.sample.leantech.transfer.model.context;

import lombok.Getter;

public class JiraTransferContext extends TransferContext {

    @Getter
    private final JiraResult jiraResult;

    public JiraTransferContext(Source source) {
        super(source);
        this.jiraResult = new JiraResult();
    }

}