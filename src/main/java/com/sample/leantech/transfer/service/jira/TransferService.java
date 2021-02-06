package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.service.repository.IRepository;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import com.sample.leantech.transfer.task.transform.TransformTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final List<ExtractTask> extractTasks;
    private final List<TransformTask> transformTasks;

    private volatile boolean working;

    @Autowired
    @Qualifier("userSparkRepository")
    IRepository userRepository;

    @Scheduled(fixedRateString = "${transfer.milliseconds}")
    public void transfer() {
        if (working) {
            log.info("Transfer is already started");
            return;
        }
        working = true;
        log.info("Transfer is started");

        Integer logId = generateLogId();
        TransferContext ctx = new TransferContext(logId);
        extractData(ctx);
        transformData(ctx);
        loadData(ctx);

        log.info("Transfer is finished");
        working = false;
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
        // TODO: implement
    }

}