package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraProjectClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
@RequiredArgsConstructor
public class ProjectExtractTask implements ExtractTask {

    private final JiraProjectClient jiraProjectClient;

    @Override
    public void extract(TransferContext ctx) {
        // TODO: implement (get projects and save in ctx)
    }

}