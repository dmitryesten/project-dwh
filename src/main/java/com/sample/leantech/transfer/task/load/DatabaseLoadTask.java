package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseLoadTask implements LoadTask {

    @Override
    public void load(TransferContext ctx) {
        // TODO: implement
    }

}