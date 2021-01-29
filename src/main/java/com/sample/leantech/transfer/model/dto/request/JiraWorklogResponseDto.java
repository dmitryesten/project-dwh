package com.sample.leantech.transfer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JiraWorklogResponseDto {

    private List<JiraWorklogDto> worklogs;

}