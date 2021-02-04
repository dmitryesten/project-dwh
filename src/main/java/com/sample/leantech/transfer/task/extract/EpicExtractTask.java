package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(value = 3)
@RequiredArgsConstructor
public class EpicExtractTask implements ExtractTask {

    private final JiraClient jiraClient;

    @Override
    public void extract(TransferContext ctx) {
        List<JiraIssueDto> projects = jiraClient.getEpics().getIssues();
        ctx.setEpics(projects);
    }

}