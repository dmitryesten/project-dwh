package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.task.extract.ExtractTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final List<ExtractTask> extractTasks;

    private volatile boolean working;

    @Scheduled(fixedRateString = "${transfer.milliseconds}")
    public void transfer() {
        if (working) {
            log.info("Transfer is already started");
            return;
        }
        working = true;
        log.info("Transfer is started");

        TransferContext ctx = new TransferContext();
        ctx.setSource(Source.JIRA_1);
        extractData(ctx);
        loadData(ctx);

        log.info("Transfer is finished");
        working = false;
    }

    private void extractData(TransferContext ctx) {
        extractTasks.forEach(task -> task.extract(ctx));
    }

    private void loadData(TransferContext ctx) {
        // TODO: implement
    }

}