package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraResult;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("tests,default")
public abstract class AbstractMapperTest {

    JiraResult jiraResult() {
        TransferContext ctx = new TransferContext();
        ctx.setLogId(1);
        return ctx.jiraResult(Source.JIRA_1);
    }

}