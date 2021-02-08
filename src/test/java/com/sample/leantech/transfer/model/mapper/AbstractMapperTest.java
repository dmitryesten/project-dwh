package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraTransferContext;
import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"tests", "default"})
public abstract class AbstractMapperTest {

    TransferContext transferContext() {
        return new JiraTransferContext(1, Source.JIRA);
    }

}