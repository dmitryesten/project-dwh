package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import com.sample.leantech.transfer.task.load.LoadTask;
import com.sample.leantech.transfer.task.transform.TransformTask;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jiraService")
public class JiraTransferService extends TransferService<JiraTransferContext> {

    public JiraTransferService(@Qualifier("jiraExtractTasks") List<ExtractTask<JiraTransferContext>> jiraExtractTasks,
                               @Qualifier("jiraTransformTasks") List<TransformTask<JiraTransferContext>> jiraTransformTasks,
                               @Qualifier("loadTasks") List<LoadTask> loadTasks) {
        super(jiraExtractTasks, jiraTransformTasks, loadTasks);
    }

    @Scheduled(fixedRateString = "${transfer.jira.milliseconds}")
    @Override
    public void transfer() {
        transfer(Source.JIRA);
    }

    @Override
    JiraTransferContext transferContext(Source source) {
        return new JiraTransferContext(source);
    }

}