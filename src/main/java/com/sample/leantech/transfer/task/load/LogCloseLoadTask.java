package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(6)
@Component
@RequiredArgsConstructor
public class LogCloseLoadTask implements LoadTask {

    @Autowired
    @Qualifier("logTransferSparkRepository")
    IRepository logTransferSparkRepository;

    @Override
    public void load(TransferContext ctx) {
        // TODO: implement
    }

}