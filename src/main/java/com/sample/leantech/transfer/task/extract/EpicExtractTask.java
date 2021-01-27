package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraIssueClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 3)
@RequiredArgsConstructor
public class EpicExtractTask implements ExtractTask {

    private JiraIssueClient jiraIssueClient;

    @Override
    public void extract(TransferContext ctx) {
        // TODO: implement (get first-level issues and save in ctx)
    }

}