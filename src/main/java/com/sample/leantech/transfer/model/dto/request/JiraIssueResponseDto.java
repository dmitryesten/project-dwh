package com.sample.leantech.transfer.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class JiraIssueResponseDto {

    private List<JiraIssueDto> issues;

}