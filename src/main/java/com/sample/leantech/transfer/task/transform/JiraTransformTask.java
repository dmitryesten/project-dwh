package com.sample.leantech.transfer.task.transform;

import com.sample.leantech.transfer.model.context.*;
import com.sample.leantech.transfer.model.db.*;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component("jiraTransformTask")
public class JiraTransformTask implements TransformTask<JiraTransferContext> {

    @Override
    public Source source() {
        return Source.JIRA;
    }

    @Override
    public void transform(JiraTransferContext ctx) {
        transformProjects(ctx);
        transformIssues(ctx);
        transformIssueFields(ctx);
        transformWorklogs(ctx);
        transformUsers(ctx);
    }

    private void transformProjects(JiraTransferContext ctx) {
        List<Project> projects = ctx.getJiraResult()
                .getProjects()
                .stream()
                .map(project -> ProjectMapper.INSTANCE.dtoToModel(project, ctx))
                .collect(Collectors.toList());
        ctx.getDatabaseModel().getProjects().addAll(projects);
        projects.clear();
        ctx.getJiraResult().getProjects().clear();
    }

    private void transformIssues(JiraTransferContext ctx) {
        List<Issue> issues = Stream.<List<JiraIssueDto>>builder()
                .add(ctx.getJiraResult().getEpics())
                .add(ctx.getJiraResult().getIssues())
                .build()
                .flatMap(Collection::stream)
                .map(issue -> IssueMapper.INSTANCE.dtoToModel(issue, ctx))
                .sorted(Comparator.comparing(Issue::getSourceId))
                .collect(Collectors.toList());
        ctx.getDatabaseModel().getIssues().addAll(issues);
        //issues.clear();
        //ctx.getJiraResult().getIssues().clear();
    }

    private void transformIssueFields(JiraTransferContext ctx) {
        List<JiraIssueDto> issueFields = Stream.<List<JiraIssueDto>>builder()
                .add(ctx.getJiraResult().getEpics())
                .add(ctx.getJiraResult().getIssues())
                .build()
                .flatMap(Collection::stream)
                .filter(issueDto -> issueDto.getFields().getIssuetype().getName().equals("Project"))
                .peek(issueDto -> {
                    if(!issueDto.getFields().getComponents().isEmpty()) {
                        issueDto.getFields().getComponents()
                                .forEach(component -> { component.setIssueId(issueDto.getId()); });
                    }
                }).collect(Collectors.toList());

        List<IssueField> issueFieldsCustomfield =
                issueFields.stream().filter(issueDto -> Optional.ofNullable(issueDto.getFields().getCustomfield()).isPresent())
                .map(issueDto -> IssueFieldMapper.INSTANCE.dtoToModel(issueDto, ctx))
                .collect(Collectors.toCollection(LinkedList::new));

        List<JiraIssueDto.Fields.Component> issueComponentsDto = issueFields.stream()
                .map(issueDto -> issueDto.getFields().getComponents())
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedList::new));

        List<IssueField> issueFieldsWithComponent = issueComponentsDto.stream()
                .flatMap(componentDto -> issueFields.stream()
                        .filter(issueDto -> issueDto.getId().equals(componentDto.getIssueId()))
                        .peek(issueDto -> {issueDto.getFields().setComponent(componentDto);}))
                .map(issueDto -> IssueFieldMapper.INSTANCE.dtoToModel(issueDto, ctx))
                .collect(Collectors.toCollection(LinkedList::new));

        ctx.getDatabaseModel().getIssueFields().addAll(issueFieldsCustomfield);
        ctx.getDatabaseModel().getIssueFields().addAll(issueFieldsWithComponent);
    }

    private void transformWorklogs(JiraTransferContext ctx) {
        List<Worklog> worklogs = ctx.getJiraResult()
                .getWorklogs()
                .stream()
                .map(worklog -> WorklogMapper.INSTANCE.dtoToModel(worklog, ctx))
                .collect(Collectors.toList());
        ctx.getDatabaseModel().getWorklogs().addAll(worklogs);
        worklogs.clear();
        ctx.getJiraResult().getWorklogs().clear();
    }

    private void transformUsers(JiraTransferContext ctx) {
        List<User> users = ctx.getJiraResult()
                .getUsers()
                .stream()
                .map(user -> UserMapper.INSTANCE.dtoToModel(user, ctx))
                .collect(Collectors.toList());
        ctx.getDatabaseModel().getUsers().addAll(users);
        users.clear();
        ctx.getJiraResult().getWorklogs().clear();
    }

}