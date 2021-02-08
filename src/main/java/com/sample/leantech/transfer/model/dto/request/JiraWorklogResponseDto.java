package com.sample.leantech.transfer.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class JiraWorklogResponseDto {

    private List<JiraWorklogDto> worklogs;

}