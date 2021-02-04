package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraProjectClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(value = 2)
@RequiredArgsConstructor
public class ProjectExtractTask implements ExtractTask {

    private final JiraProjectClient jiraProjectClient;

    @Override
    public void extract(TransferContext ctx) {
        List<JiraProjectDto> projects = jiraProjectClient.getProjects();
        ctx.setProjects(projects);
    }

}