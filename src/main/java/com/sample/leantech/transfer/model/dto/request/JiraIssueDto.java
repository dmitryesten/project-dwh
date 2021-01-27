package com.sample.leantech.transfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssueDto {
    private String id;
    private String expand;
    private String self;
    private String key;
    private String created;
    private String summary;
    private String description;
    private int timespent;
}
