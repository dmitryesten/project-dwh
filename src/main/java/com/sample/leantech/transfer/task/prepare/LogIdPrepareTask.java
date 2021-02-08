package com.sample.leantech.transfer.task.prepare;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.LogTransfer;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component("logIdPrepareTask")
@Slf4j
public class LogIdPrepareTask implements PrepareTask {

    private final IRepository logTransferSparkRepository;

    public LogIdPrepareTask(@Qualifier("logTransferSparkRepository") IRepository logTransferSparkRepository) {
        this.logTransferSparkRepository = logTransferSparkRepository;
    }

    @Override
    public void prepare(TransferContext ctx) {
        // TODO: use sequence
        Integer maxLogId = logTransferSparkRepository.get()
                .stream()
                .map(LogTransfer.class::cast)
                .map(LogTransfer::getId)
                .max(Comparator.naturalOrder())
                .orElse(0);
        Integer nextLogId = maxLogId + 1;
        ctx.getLogInfo().setLogId(nextLogId);
        log.info("Id нового лога = "+ nextLogId);
    }

}