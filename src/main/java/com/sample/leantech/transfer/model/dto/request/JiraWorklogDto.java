package com.sample.leantech.transfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraWorklogDto {

    private JiraUserDto updateAuthor;
    //private ZonedDateTime updated;
    private Long timeSpentSeconds;
    private String id;
    private String issueId;

}