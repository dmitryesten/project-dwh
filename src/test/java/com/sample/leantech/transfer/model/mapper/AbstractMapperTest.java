package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraResult;
import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"tests", "default"})
public abstract class AbstractMapperTest {

    JiraResult jiraResult() {
        JiraTransferContext ctx = new JiraTransferContext(1, Source.JIRA);
        return ctx.getJiraResult();
    }

}