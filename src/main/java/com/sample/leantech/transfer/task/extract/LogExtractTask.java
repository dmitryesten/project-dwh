package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;

@Component
@Order(value = 1)
@RequiredArgsConstructor
public class LogExtractTask implements ExtractTask {

    private final Clock clock;

    @Override
    public void extract(TransferContext ctx) {
        // TODO: generate log ID
        ctx.setStartDateTime(ZonedDateTime.now(clock));
    }

}