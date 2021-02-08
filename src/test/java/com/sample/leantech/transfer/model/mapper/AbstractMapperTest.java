package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"tests", "default"})
public abstract class AbstractMapperTest {

    TransferContext transferContext() {
        TransferContext ctx = new JiraTransferContext(Source.JIRA);
        ctx.getLogInfo().setLogId(1);
        return ctx;
    }

}