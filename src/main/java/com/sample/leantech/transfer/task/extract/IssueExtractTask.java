package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraIssueClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraIssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Order(value = 3)
@RequiredArgsConstructor
public class IssueExtractTask implements ExtractTask {

    private final JiraIssueClient jiraIssueClient;
    private final JavaSparkContext spark;

    @Override
    public void extract(TransferContext ctx) {
        List<JiraIssueDto> issues = new ArrayList<>();
        extractNonEpicIssues(issues);
        extractEpicIssues(ctx, issues);
        ctx.setIssues(spark.parallelize(issues));
    }

    private void extractNonEpicIssues(List<JiraIssueDto> issues) {
        List<JiraIssueDto> nonEpicIssues = jiraIssueClient.getNonEpicIssues().getIssues();
        issues.addAll(nonEpicIssues);
    }

    private void extractEpicIssues(TransferContext ctx, List<JiraIssueDto> issues) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        ctx.getEpics().foreach((VoidFunction<JiraIssueDto>) epic -> futures.add(appendEpicIssues(epic, issues)));
        futures.forEach(CompletableFuture::join);
    }

    private CompletableFuture<Boolean> appendEpicIssues(JiraIssueDto epic, List<JiraIssueDto> issues) {
        return jiraIssueClient.getEpicIssuesAsync(epic.getId())
                .thenApply(JiraIssueResponseDto::getIssues)
                .thenApply(issues::addAll);
    }

}