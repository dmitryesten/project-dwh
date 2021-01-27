package com.sample.leantech.transfer.service.jira;

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
            log.info("Transfer is already working");
        }
        working = true;
        log.info("Transfer is started");
        // TODO: implement
        log.info("Transfer is finished");
        working = false;
    }

}