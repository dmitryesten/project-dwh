package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraWorklogClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Order(value = 5)
@RequiredArgsConstructor
public class WorklogExtractTask implements ExtractTask {

    private final JiraWorklogClient jiraWorklogClient;
    private final JavaSparkContext spark;

    @Override
    public void extract(TransferContext ctx) {
        List<JiraWorklogDto> worklogs = new ArrayList<>();
        // Во всех issue, кроме эпиков, изначально содержатся логи работы.
        // Для эпиков приходится выполнять дополнительные запросы.
        extractNonEpicWorklogs(ctx, worklogs);
        extractEpicWorklogs(ctx, worklogs);
        ctx.setWorklogs(spark.parallelize(worklogs));
    }

    private void extractNonEpicWorklogs(TransferContext ctx, List<JiraWorklogDto> worklogs) {
        List<JiraWorklogDto> nonEpicWorklogs = ctx.getIssues()
                .map(JiraIssueDto::getFields)
                .map(JiraIssueDto.Fields::getWorklog)
                .map(JiraWorklogResponseDto::getWorklogs)
                .collect()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        worklogs.addAll(nonEpicWorklogs);
    }

    private void extractEpicWorklogs(TransferContext ctx, List<JiraWorklogDto> worklogs) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        ctx.getEpics().foreach((VoidFunction<JiraIssueDto>) epic ->
                futures.add(appendEpicWorklogs(epic, worklogs)));
        futures.forEach(CompletableFuture::join);
    }

    private CompletableFuture<Boolean> appendEpicWorklogs(JiraIssueDto epic, List<JiraWorklogDto> worklogs) {
        return jiraWorklogClient.getWorklogsAsync(epic.getId())
                .thenApply(JiraWorklogResponseDto::getWorklogs)
                .thenApply(worklogs::addAll);
    }

}