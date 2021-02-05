package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraClient;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JiraExtractTask implements ExtractTask {

    private final JiraClient jiraClient;

    @Override
    public void extract(TransferContext ctx) {
        extractProjects(ctx);
        extractEpics(ctx);
        extractIssues(ctx);
        extractWorklogs(ctx);
        extractUsers(ctx);
    }

    private void extractProjects(TransferContext ctx) {
        List<JiraProjectDto> projects = jiraClient().getProjects();
        ctx.setProjects(projects);
    }

    private void extractEpics(TransferContext ctx) {
        List<JiraIssueDto> epics = jiraClient().getEpics().getIssues();
        ctx.setEpics(epics);
    }

    private void extractIssues(TransferContext ctx) {
        List<JiraIssueDto> issues = new ArrayList<>();
        extractNonEpicIssues(issues);
        extractEpicIssues(ctx, issues);
        ctx.setIssues(issues);
    }

    private void extractNonEpicIssues(List<JiraIssueDto> issues) {
        List<JiraIssueDto> nonEpicIssues = jiraClient().getNonEpicIssues().getIssues();
        issues.addAll(nonEpicIssues);
    }

    private void extractEpicIssues(TransferContext ctx, List<JiraIssueDto> issues) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        ctx.getEpics().forEach((JiraIssueDto epic) -> futures.add(appendEpicIssues(epic, issues)));
        futures.forEach(CompletableFuture::join);
    }

    private CompletableFuture<Boolean> appendEpicIssues(JiraIssueDto epic, List<JiraIssueDto> issues) {
        return jiraClient().getEpicIssuesAsync(epic.getId())
                .thenApply(JiraIssueResponseDto::getIssues)
                .thenApply(issues::addAll);
    }

    private void extractWorklogs(TransferContext ctx) {
        List<JiraWorklogDto> worklogs = new ArrayList<>();
        // Во всех issue, кроме эпиков, изначально содержатся логи работы.
        // Для эпиков приходится выполнять дополнительные запросы.
        extractNonEpicWorklogs(ctx, worklogs);
        extractEpicWorklogs(ctx, worklogs);
        ctx.setWorklogs(worklogs);
    }

    private void extractNonEpicWorklogs(TransferContext ctx, List<JiraWorklogDto> worklogs) {
        List<JiraWorklogDto> nonEpicWorklogs = ctx.getIssues()
                .stream()
                .map(JiraIssueDto::getFields)
                .map(JiraIssueDto.Fields::getWorklog)
                .map(JiraWorklogResponseDto::getWorklogs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        worklogs.addAll(nonEpicWorklogs);
    }

    private void extractEpicWorklogs(TransferContext ctx, List<JiraWorklogDto> worklogs) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        ctx.getEpics().forEach((JiraIssueDto epic) -> futures.add(appendEpicWorklogs(epic, worklogs)));
        futures.forEach(CompletableFuture::join);
    }

    private CompletableFuture<Boolean> appendEpicWorklogs(JiraIssueDto epic, List<JiraWorklogDto> worklogs) {
        return jiraClient().getWorklogsAsync(epic.getId())
                .thenApply(JiraWorklogResponseDto::getWorklogs)
                .thenApply(worklogs::addAll);
    }

    private void extractUsers(TransferContext ctx) {
        List<JiraUserDto> users = ctx.getWorklogs()
                .stream()
                .map(JiraWorklogDto::getUpdateAuthor)
                .distinct()
                .collect(Collectors.toList());
        ctx.setUsers(users);
    }

    JiraClient jiraClient() {
        return jiraClient;
    }

}