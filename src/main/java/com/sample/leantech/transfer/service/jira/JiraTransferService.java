package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import com.sample.leantech.transfer.task.load.LoadTask;
import com.sample.leantech.transfer.task.transform.TransformTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jiraService")
public class JiraTransferService extends TransferService<JiraTransferContext> {

    @Autowired
    @Qualifier("jiraExtractTasks")
    private List<ExtractTask<JiraTransferContext>> jiraExtractTasks;

    @Autowired
    @Qualifier("jiraTransformTasks")
    private List<TransformTask<JiraTransferContext>> jiraTransformTasks;

    @Autowired
    @Qualifier("loadTasks")
    private List<LoadTask> loadTasks;

    @Scheduled(fixedRateString = "${transfer.jira.milliseconds}")
    @Override
    public void transfer() {
        transfer(Source.JIRA);
    }

    @Override
    JiraTransferContext transferContext(Integer logId, Source source) {
        return new JiraTransferContext(logId, source);
    }

    Integer logId() {
        // TODO: implement logId generation
        return null;
    }

    @Override
    List<ExtractTask<JiraTransferContext>> extractTasks() {
        return jiraExtractTasks;
    }

    @Override
    List<TransformTask<JiraTransferContext>> transformTasks() {
        return jiraTransformTasks;
    }

    @Override
    List<LoadTask> loadTasks() {
        return loadTasks;
    }

}