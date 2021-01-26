package com.sample.leantech.transfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraProjectRequestDto {

    private String expand;
    private String self;
    private String id;
    private String key;
    private String name;
    private String projectTypeKey;
    private boolean archived;

}