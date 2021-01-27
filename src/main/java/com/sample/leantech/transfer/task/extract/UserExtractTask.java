package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraUserClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
@RequiredArgsConstructor
public class UserExtractTask implements ExtractTask {

    private final JiraUserClient jiraUserClient;

    @Override
    public void extract(TransferContext ctx) {
        // TODO: implement (get users and save in ctx)
    }

}