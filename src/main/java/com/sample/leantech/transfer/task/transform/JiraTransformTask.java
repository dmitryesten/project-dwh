package com.sample.leantech.transfer.task.transform;

import com.sample.leantech.transfer.model.context.*;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.mapper.IssueMapper;
import com.sample.leantech.transfer.model.mapper.ProjectMapper;
import com.sample.leantech.transfer.model.mapper.UserMapper;
import com.sample.leantech.transfer.model.mapper.WorklogMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("jiraTransformTask")
public class JiraTransformTask implements TransformTask<JiraTransferContext> {

    @Override
    public Source source() {
        return Source.JIRA;
    }

    @Override
    public void transform(JiraTransferContext ctx) {
        List<JiraResult> jiraResults = ctx.getJiraResults();
        DatabaseModel databaseModel = ctx.getDatabaseModel();
        jiraResults.forEach(jiraResult -> transform(jiraResult, databaseModel));
    }

    private void transform(JiraResult jiraResult, DatabaseModel databaseModel) {
        transformProjects(jiraResult, databaseModel);
        transformIssues(jiraResult, databaseModel);
        transformWorklogs(jiraResult, databaseModel);
        transformUsers(jiraResult, databaseModel);
    }

    private void transformProjects(JiraResult jiraResult, DatabaseModel databaseModel) {
        List<Project> projects = jiraResult.getProjects()
                .stream()
                .map(project -> ProjectMapper.INSTANCE.dtoToModel(project, jiraResult))
                .collect(Collectors.toList());
        databaseModel.getProjects().addAll(projects);
    }

    private void transformIssues(JiraResult jiraResult, DatabaseModel databaseModel) {
        List<Issue> issues = jiraResult.getIssues()
                .stream()
                .map(issue -> IssueMapper.INSTANCE.dtoToModel(issue, jiraResult))
                .collect(Collectors.toList());
        databaseModel.getIssues().addAll(issues);
    }

    private void transformWorklogs(JiraResult jiraResult, DatabaseModel databaseModel) {
        List<Worklog> worklogs = jiraResult.getWorklogs()
                .stream()
                .map(worklog -> WorklogMapper.INSTANCE.dtoToModel(worklog, jiraResult))
                .collect(Collectors.toList());
        databaseModel.getWorklogs().addAll(worklogs);
    }

    private void transformUsers(JiraResult jiraResult, DatabaseModel databaseModel) {
        List<User> users = jiraResult.getUsers()
                .stream()
                .map(user -> UserMapper.INSTANCE.dtoToModel(user, jiraResult))
                .collect(Collectors.toList());
        databaseModel.getUsers().addAll(users);
    }

}