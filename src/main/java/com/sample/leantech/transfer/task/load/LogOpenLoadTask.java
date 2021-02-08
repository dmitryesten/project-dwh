package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.LogTransfer;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class LogOpenLoadTask implements LoadTask {

    @Autowired
    @Qualifier("logTransferSparkRepository")
    IRepository logTransferSparkRepository;

    public void load(TransferContext ctx) {
        LogTransfer logTransfer = new LogTransfer();
        logTransfer.setId(ctx.getLogInfo().getLogId());
        logTransfer.setSid(ctx.getSource().getValue());
        logTransfer.setStartDt(Timestamp.from(ctx.getLogInfo().getStartDateTime().toInstant()));
        Collection<LogTransfer> logTransferCollection = Arrays.asList(logTransfer);
        log.info("Запись логс");
        logTransferSparkRepository.save(logTransferCollection);
        log.info("Завершение записи");
        int idLogs = logTransferSparkRepository.get()
                .stream()
                .map(LogTransfer.class::cast)
                .filter(s -> Optional.ofNullable(s.getEndDt()).isEmpty())
                .findFirst()
                .get()
                .getId();
        log.info("Id созданного лога = "+ idLogs );
    }

}