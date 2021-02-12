package com.sample.leantech.transfer.task.transform;

import com.sample.leantech.transfer.model.context.*;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.mapper.IssueMapper;
import com.sample.leantech.transfer.model.mapper.ProjectMapper;
import com.sample.leantech.transfer.model.mapper.UserMapper;
import com.sample.leantech.transfer.model.mapper.WorklogMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    }

    private void transformIssues(JiraTransferContext ctx) {
        List<Issue> issues = Stream.<List<JiraIssueDto>>builder()
                .add(ctx.getJiraResult().getEpics())
                .add(ctx.getJiraResult().getIssues())
                .build()
                .flatMap(Collection::stream)
                .map(issue -> IssueMapper.INSTANCE.dtoToModel(issue, ctx))
                .collect(Collectors.toList());
        ctx.getDatabaseModel().getIssues().addAll(issues);
    }

    private void transformWorklogs(JiraTransferContext ctx) {
        List<Worklog> worklogs = ctx.getJiraResult()
                .getWorklogs()
                .stream()
                .map(worklog -> WorklogMapper.INSTANCE.dtoToModel(worklog, ctx))
                .collect(Collectors.toList());
        ctx.getDatabaseModel().getWorklogs().addAll(worklogs);
    }

    private void transformUsers(JiraTransferContext ctx) {
        List<User> users = ctx.getJiraResult()
                .getUsers()
                .stream()
                .map(user -> UserMapper.INSTANCE.dtoToModel(user, ctx))
                .collect(Collectors.toList());
        ctx.getDatabaseModel().getUsers().addAll(users);
    }

}