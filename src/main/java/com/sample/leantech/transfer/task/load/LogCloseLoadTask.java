package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.LogTransfer;
import com.sample.leantech.transfer.service.repository.LogTransferSparkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Order(5)
@Component
@RequiredArgsConstructor
public class LogCloseLoadTask implements LoadTask {

    @Autowired
    @Qualifier("logTransferSparkRepository")
    LogTransferSparkRepository logTransferSparkRepository;

    @Override
    public void load(TransferContext ctx) {
        LogTransfer closeLogTransfer = new LogTransfer();
        closeLogTransfer.setHid(ctx.getLogInfo().getLogId());
        closeLogTransfer.setSid(ctx.getSource().getValue());
        closeLogTransfer.setEndDt(Timestamp.from(Instant.now()));
        closeLogTransfer.setResult(true);
        logTransferSparkRepository.closeOpenedLogTransfer(closeLogTransfer);
    }

}