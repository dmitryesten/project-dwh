package com.sample.leantech.transfer.config;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import com.sample.leantech.transfer.task.load.LoadTask;
import com.sample.leantech.transfer.task.transform.TransformTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TaskConfig {

    @Autowired
    @Qualifier("jiraExtractTask")
    private ExtractTask<JiraTransferContext> jiraExtractTask;

    @Autowired
    @Qualifier("jiraTransformTask")
    private TransformTask<JiraTransferContext> jiraTransformTask;

    @Autowired
    private List<LoadTask> loadTasks;

    @Bean("jiraExtractTasks")
    public List<ExtractTask<JiraTransferContext>> jiraExtractTasks() {
        return List.of(jiraExtractTask);
    }

    @Bean("jiraTransformTasks")
    public List<TransformTask<JiraTransferContext>> jiraTransformTasks() {
        return List.of(jiraTransformTask);
    }

    @Bean("loadTasks")
    public List<LoadTask> loadTasks() {
        return loadTasks;
    }

}