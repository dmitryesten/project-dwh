package com.sample.leantech.transfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraUserDto {

    private String self;
    private String key;
    private String name;
    private String emailAddress;
    private String displayName;
    private boolean active;
    private boolean deleted;
    private String timeZone;
    private String locale;

}