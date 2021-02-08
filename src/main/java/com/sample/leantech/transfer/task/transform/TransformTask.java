package com.sample.leantech.transfer.task.transform;

import com.sample.leantech.transfer.model.context.TransferContext;

public interface TransformTask {

    void transform(TransferContext ctx);

}