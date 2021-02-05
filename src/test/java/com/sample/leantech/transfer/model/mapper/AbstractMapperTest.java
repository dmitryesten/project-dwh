package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("tests,default")
public abstract class AbstractMapperTest {

    TransferContext transferContext() {
        TransferContext ctx = new TransferContext();
        ctx.setSource(Source.JIRA_1);
        ctx.setLogId(1);
        return ctx;
    }

}