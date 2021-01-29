package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.dto.request.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.spark.rdd.RDD;

@Data
@NoArgsConstructor
public class TransferContext {

    private RDD<JiraProjectDto> projects;
    private RDD<JiraIssueDto> epics;
    private RDD<JiraIssueDto> issues;
    private RDD<JiraWorklogDto> worklogs;
    private RDD<JiraUserDto> users;

}