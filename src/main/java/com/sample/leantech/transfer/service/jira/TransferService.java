package com.sample.leantech.transfer.service.jira;

import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private volatile boolean working;

    public void transfer() {
        if (working) {
            log.info("Transfer is already started");
            return;
        }
        working = true;
        log.info("Transfer is started");

        TransferContext ctx = new TransferContext();
        extractData(ctx);
        loadData(ctx);

        log.info("Transfer is finished");
        working = false;
    }

    private void extractData(TransferContext ctx) {
        // TODO: implement
    }

    private void loadData(TransferContext ctx) {
        // TODO: implement
    }

}