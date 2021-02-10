package com.sample.leantech.transfer.task.prepare;

import com.sample.leantech.transfer.model.context.TransferContext;

public interface PrepareTask {

    void prepare(TransferContext ctx);

}