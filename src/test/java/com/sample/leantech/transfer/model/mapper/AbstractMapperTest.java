package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"tests", "default"})
public abstract class AbstractMapperTest {

    static final Integer LOG_ID = 1;
    static final String JIRA_USER_PREFIX = "JIRAUSER";
    static final String USER_ID = "10001";

    TransferContext transferContext() {
        TransferContext ctx = new JiraTransferContext(Source.JIRA);
        ctx.getLogInfo().setLogId(LOG_ID);
        return ctx;
    }

}