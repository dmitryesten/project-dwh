package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import com.sample.leantech.transfer.task.load.LoadTask;
import com.sample.leantech.transfer.task.transform.TransformTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final List<ExtractTask> extractTasks;
    private final List<TransformTask> transformTasks;
    private final List<LoadTask> loadTasks;

    private final Map<Source, Boolean> workStatuses = new ConcurrentHashMap<>();

    @Scheduled(fixedRateString = "${transfer.jira.milliseconds}")
    public void transfer() {
        transfer(Source.JIRA);
    }

    private void transfer(Source source) {
        if (workStatuses.getOrDefault(source, false)) {
            log.info("Transfer is already started for " + source.name());
            return;
        }
        workStatuses.put(source, true);
        log.info("Transfer is started for " + source.name());

        Integer logId = generateLogId();
        TransferContext ctx = new TransferContext(logId);
        extractData(ctx);
        transformData(ctx);
        loadData(ctx);

        log.info("Transfer is finished for " + source.name());
        workStatuses.put(source, false);
    }

    private Integer generateLogId() {
        // TODO: implement
        return null;
    }

    private void extractData(TransferContext ctx) {
        extractTasks.forEach(task -> task.extract(ctx));
    }

    private void transformData(TransferContext ctx) {
        transformTasks.forEach(task -> task.transform(ctx));
    }

    private void loadData(TransferContext ctx) {
        loadTasks.forEach(task -> task.load(ctx));
    }

}