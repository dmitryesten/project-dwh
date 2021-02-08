package com.sample.leantech.transfer.model.context;

import lombok.Data;

import java.time.Clock;
import java.time.ZonedDateTime;

@Data
public class TransferContext {

    final Integer logId;
    final Source source;
    private final ZonedDateTime startDateTime;
    private final DatabaseModel databaseModel;

    public TransferContext(Integer logId, Source source) {
        this.logId = logId;
        this.source = source;
        this.startDateTime = ZonedDateTime.now(Clock.systemUTC());
        this.databaseModel = new DatabaseModel(logId);
    }

}