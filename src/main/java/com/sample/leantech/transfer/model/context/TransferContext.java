package com.sample.leantech.transfer.model.context;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TransferContext implements Serializable {

    private Integer logId;
    private ZonedDateTime startDateTime;
    private List<JiraResult> jiraResults;
    private DatabaseModel databaseModel;

    public JiraResult jiraResult(Source source) {
        JiraResult jiraResult = new JiraResult();
        jiraResult.setLogId(logId);
        jiraResult.setSource(source);
        jiraResults.add(jiraResult);
        return jiraResult;
    }

    public DatabaseModel databaseModel() {
        DatabaseModel databaseModel = new DatabaseModel();
        databaseModel.setLogId(logId);
        this.databaseModel = databaseModel;
        return databaseModel;
    }

    public static TransferContext transferContext(Integer logId) {
        TransferContext ctx = new TransferContext();
        ctx.logId = logId;
        ctx.startDateTime = ZonedDateTime.now(Clock.systemUTC());
        ctx.jiraResults = new ArrayList<>();
        ctx.databaseModel = ctx.databaseModel();
        return ctx;
    }

}