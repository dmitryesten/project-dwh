package com.sample.leantech.transfer.model.dto.request;

import lombok.Data;

@Data
public class JiraProjectRequestDto {

    private final String expand;
    private final String self;
    private final String id;
    private final String key;
    private final String name;
    private final String projectTypeKey;
    private final boolean archived;

}