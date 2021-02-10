package com.sample.leantech.transfer.task.transform;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;

public interface TransformTask<T extends TransferContext> {

    Source source();

    void transform(T ctx);

}