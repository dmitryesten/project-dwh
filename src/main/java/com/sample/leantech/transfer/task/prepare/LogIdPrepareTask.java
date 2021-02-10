package com.sample.leantech.transfer.task.prepare;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.LogTransfer;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;

@Component("logIdPrepareTask")
@Slf4j
public class LogIdPrepareTask implements PrepareTask {

    private final IRepository logTransferSparkRepository;

    public LogIdPrepareTask(@Qualifier("logTransferSparkRepository") IRepository logTransferSparkRepository) {
        this.logTransferSparkRepository = logTransferSparkRepository;
    }

    @Override
    public void prepare(TransferContext ctx) {
        LogTransfer logTransfer = createLogTransfer(ctx);
        saveLogTransfer(logTransfer);
        ctx.getLogInfo().setLogId(getCurrentLogId());
    }

    private LogTransfer createLogTransfer(TransferContext ctx) {
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.setSid(ctx.getSource().getValue());
        ZonedDateTime startDateTime = ctx.getLogInfo().getStartDateTime();
        logTransfer.setStartDt(Timestamp.from(startDateTime.toInstant()));
        return logTransfer;
    }

    private void saveLogTransfer(LogTransfer logTransfer) {
        Collection<LogTransfer> logTransfers = List.of(logTransfer);
        log.info("Запись лога");
        logTransferSparkRepository.save(logTransfers);
        log.info("Завершение записи лога");
    }

    private Integer getCurrentLogId() {
        Integer currentLogId = logTransferSparkRepository.get()
                .stream()
                .map(LogTransfer.class::cast)
                .filter(logTransfer -> logTransfer.getEndDt() == null)
                .map(LogTransfer::getId)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalStateException("Log ID not created"));
        log.info("Id созданного лога = "+ currentLogId);
        return currentLogId;
    }

}