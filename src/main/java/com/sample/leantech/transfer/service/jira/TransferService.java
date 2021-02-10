package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import com.sample.leantech.transfer.task.load.LoadTask;
import com.sample.leantech.transfer.task.prepare.PrepareTask;
import com.sample.leantech.transfer.task.transform.TransformTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public abstract class TransferService<T extends TransferContext> {

    private final List<PrepareTask> prepareTasks;
    private final List<ExtractTask<T>> extractTasks;
    private final List<TransformTask<T>> transformTasks;
    private final List<LoadTask> loadTasks;

    // TODO: fix
    private final Map<Source, Boolean> workStatuses = new ConcurrentHashMap<>();

    public abstract void transfer();

    void transfer(Source source) {
        if (workStatuses.getOrDefault(source, false)) {
            log.info("Transfer is already started for " + source.name());
            return;
        }
        workStatuses.put(source, true);
        log.info("Transfer is started for " + source.name());

        T ctx = transferContext(source);
        prepareData(ctx);
        extractData(ctx);
        transformData(ctx);
        loadData(ctx);

        log.info("Transfer is finished for " + source.name());
        workStatuses.put(source, false);
    }

    abstract T transferContext(Source source);

    void prepareData(T ctx) {
        prepareTasks.forEach(task -> task.prepare(ctx));
    }

    void extractData(T ctx) {
        extractTasks.stream()
                .filter(task -> task.source() == ctx.getSource())
                .forEach(task -> task.extract(ctx));
    }

    void transformData(T ctx) {
        transformTasks.stream()
                .filter(task -> task.source() == ctx.getSource())
                .forEach(task -> task.transform(ctx));
    }

    void loadData(T ctx) {
        loadTasks.forEach(task -> task.load(ctx));
    }

}