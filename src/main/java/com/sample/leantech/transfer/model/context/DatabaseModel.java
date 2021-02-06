package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.db.Worklog;
import lombok.Data;

import java.util.List;

@Data
public class DatabaseModel {

    private Integer logId;
    private Source source;
    private List<Project> projects;
    private List<Issue> issues;
    private List<Worklog> worklogs;
    private List<User> users;

}