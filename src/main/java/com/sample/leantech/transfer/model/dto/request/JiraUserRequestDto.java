package com.sample.leantech.transfer.model.dto.request;

import lombok.Data;

@Data
public class JiraUserRequestDto {

    private final String self;
    private final String key;
    private final String name;
    private final String emailAddress;
    private final String displayName;
    private final boolean active;
    private final boolean deleted;
    private final String timeZone;
    private final String locale;

}