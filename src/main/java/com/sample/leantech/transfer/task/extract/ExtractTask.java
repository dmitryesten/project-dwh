package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;

public interface ExtractTask {

    Source source();

    void extract(TransferContext ctx);

}