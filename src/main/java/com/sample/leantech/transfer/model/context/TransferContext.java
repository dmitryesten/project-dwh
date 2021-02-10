package com.sample.leantech.transfer.model.context;

import lombok.Data;

@Data
public class TransferContext {

    private final Source source;
    private final LogInfo logInfo;
    private final DatabaseModel databaseModel;

    public TransferContext(Source source) {
        this.source = source;
        this.logInfo = new LogInfo();
        this.databaseModel = new DatabaseModel();
    }

}