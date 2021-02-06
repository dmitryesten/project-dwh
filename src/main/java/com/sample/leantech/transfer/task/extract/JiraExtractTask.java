package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.integration.JiraClient;
import com.sample.leantech.transfer.model.context.JiraResult;
import com.sample.leantech.transfer.model.context.Source;
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
    public Source source() {
        return Source.JIRA_1;
    }

    @Override
    public void extract(TransferContext ctx) {
        JiraResult jiraResult = ctx.addJiraResult(source());
        extractProjects(jiraResult);
        extractEpics(jiraResult);
        extractIssues(jiraResult);
        extractWorklogs(jiraResult);
        extractUsers(jiraResult);
    }

    private void extractProjects(JiraResult jiraResult) {
        List<JiraProjectDto> projects = jiraClient().getProjects();
        jiraResult.setProjects(projects);
    }

    private void extractEpics(JiraResult jiraResult) {
        List<JiraIssueDto> epics = jiraClient().getEpics().getIssues();
        jiraResult.setEpics(epics);
    }

    private void extractIssues(JiraResult jiraResult) {
        List<JiraIssueDto> issues = new ArrayList<>();
        extractNonEpicIssues(issues);
        extractEpicIssues(jiraResult, issues);
        jiraResult.setIssues(issues);
    }

    private void extractNonEpicIssues(List<JiraIssueDto> issues) {
        List<JiraIssueDto> nonEpicIssues = jiraClient().getNonEpicIssues().getIssues();
        issues.addAll(nonEpicIssues);
    }

    private void extractEpicIssues(JiraResult jiraResult, List<JiraIssueDto> issues) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        jiraResult.getEpics().forEach((JiraIssueDto epic) -> futures.add(appendEpicIssues(epic, issues)));
        futures.forEach(CompletableFuture::join);
    }

    private CompletableFuture<Boolean> appendEpicIssues(JiraIssueDto epic, List<JiraIssueDto> issues) {
        return jiraClient().getEpicIssuesAsync(epic.getId())
                .thenApply(JiraIssueResponseDto::getIssues)
                .thenApply(issues::addAll);
    }

    private void extractWorklogs(JiraResult jiraResult) {
        List<JiraWorklogDto> worklogs = new ArrayList<>();
        // Во всех issue, кроме эпиков, изначально содержатся логи работы.
        // Для эпиков приходится выполнять дополнительные запросы.
        extractNonEpicWorklogs(jiraResult, worklogs);
        extractEpicWorklogs(jiraResult, worklogs);
        jiraResult.setWorklogs(worklogs);
    }

    private void extractNonEpicWorklogs(JiraResult jiraResult, List<JiraWorklogDto> worklogs) {
        List<JiraWorklogDto> nonEpicWorklogs = jiraResult.getIssues()
                .stream()
                .map(JiraIssueDto::getFields)
                .map(JiraIssueDto.Fields::getWorklog)
                .map(JiraWorklogResponseDto::getWorklogs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        worklogs.addAll(nonEpicWorklogs);
    }

    private void extractEpicWorklogs(JiraResult jiraResult, List<JiraWorklogDto> worklogs) {
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        jiraResult.getEpics().forEach((JiraIssueDto epic) ->
                futures.add(appendEpicWorklogs(epic, worklogs)));
        futures.forEach(CompletableFuture::join);
    }

    private CompletableFuture<Boolean> appendEpicWorklogs(JiraIssueDto epic, List<JiraWorklogDto> worklogs) {
        return jiraClient().getWorklogsAsync(epic.getId())
                .thenApply(JiraWorklogResponseDto::getWorklogs)
                .thenApply(worklogs::addAll);
    }

    private void extractUsers(JiraResult jiraResult) {
        List<JiraUserDto> users = jiraResult.getWorklogs()
                .stream()
                .map(JiraWorklogDto::getUpdateAuthor)
                .distinct()
                .collect(Collectors.toList());
        jiraResult.setUsers(users);
    }

    JiraClient jiraClient() {
        return jiraClient;
    }

}