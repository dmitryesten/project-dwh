package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.db.Worklog;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DatabaseModel {

    private final Integer parentLogId;
    private final List<Project> projects;
    private final List<Issue> issues;
    private final List<Worklog> worklogs;
    private final List<User> users;

    DatabaseModel(Integer parentLogId) {
        this.parentLogId = parentLogId;
        this.projects = new ArrayList<>();
        this.issues = new ArrayList<>();
        this.worklogs = new ArrayList<>();
        this.users = new ArrayList<>();
    }

}