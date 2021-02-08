package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import com.sample.leantech.transfer.task.load.LoadTask;
import com.sample.leantech.transfer.task.transform.TransformTask;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jiraService")
@RequiredArgsConstructor
public class JiraTransferService extends TransferService<JiraTransferContext> {

    private final List<ExtractTask<JiraTransferContext>> extractTasks;
    private final List<TransformTask<JiraTransferContext>> transformTasks;
    private final List<LoadTask> loadTasks;

    @Scheduled(fixedRateString = "${transfer.jira.milliseconds}")
    @Override
    public void transfer() {
        transfer(Source.JIRA);
    }

    @Override
    JiraTransferContext createTransferContext(Integer logId, Source source) {
        return new JiraTransferContext(logId);
    }

    Integer generateLogId() {
        // TODO: implement
        return null;
    }

    void extractData(JiraTransferContext ctx) {
        extractTasks.stream()
                // TODO: get from ctx
                .filter(task -> task.source() == Source.JIRA)
                .forEach(task -> task.extract(ctx));
    }

    void transformData(JiraTransferContext ctx) {
        transformTasks.stream()
                .filter(task -> task.source() == Source.JIRA)
                .forEach(task -> task.transform(ctx));
    }

    void loadData(JiraTransferContext ctx) {
        loadTasks.forEach(task -> task.load(ctx));
    }

}