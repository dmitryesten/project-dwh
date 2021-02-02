package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraIssueClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import lombok.RequiredArgsConstructor;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(value = 3)
@RequiredArgsConstructor
public class EpicExtractTask implements ExtractTask {

    private final JiraIssueClient jiraIssueClient;
    private final JavaSparkContext spark;

    @Override
    public void extract(TransferContext ctx) {
        List<JiraIssueDto> projects = jiraIssueClient.getEpics().getIssues();
        ctx.setEpics(spark.parallelize(projects));
    }

}