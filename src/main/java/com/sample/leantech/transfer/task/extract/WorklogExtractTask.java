package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 5)
@RequiredArgsConstructor
public class WorklogExtractTask implements ExtractTask {

    @Override
    public void extract(TransferContext ctx) {
        // TODO: implement (get worklogs and save in ctx)
    }

}