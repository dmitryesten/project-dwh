package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class LogLoadTask {

    public void load(TransferContext ctx) {

    }
}
