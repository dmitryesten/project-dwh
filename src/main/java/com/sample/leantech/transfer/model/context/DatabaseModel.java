package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.db.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class DatabaseModel {

    private final List<Project> projects;
    private final List<Issue> issues;
    private final List<IssueField> issueFields;
    private final List<Worklog> worklogs;
    private final List<User> users;

    DatabaseModel() {
        this.projects = new ArrayList<>();
        this.issues = new ArrayList<>();
        this.issueFields = new LinkedList<>();
        this.worklogs = new ArrayList<>();
        this.users = new ArrayList<>();
    }

}