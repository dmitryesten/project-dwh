package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class TransferService<T extends TransferContext> {

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

        Integer logId = generateLogId();
        T ctx = createTransferContext(logId, source);
        extractData(ctx);
        transformData(ctx);
        loadData(ctx);

        log.info("Transfer is finished for " + source.name());
        workStatuses.put(source, false);
    }

    abstract T createTransferContext(Integer logId, Source source);

    abstract Integer generateLogId();

    abstract void extractData(T ctx);

    abstract void transformData(T ctx);

    abstract void loadData(T ctx);

}