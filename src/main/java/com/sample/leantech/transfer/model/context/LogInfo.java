package com.sample.leantech.transfer.model.context;

import lombok.Data;

import java.time.Clock;
import java.time.ZonedDateTime;

@Data
public class LogInfo {

    private Integer logId;
    private final ZonedDateTime startDateTime;

    public LogInfo() {
        this.startDateTime = ZonedDateTime.now(Clock.systemUTC());
    }

}