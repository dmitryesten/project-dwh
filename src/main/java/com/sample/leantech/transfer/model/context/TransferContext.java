package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.dto.request.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.spark.api.java.JavaRDD;

@Data
@NoArgsConstructor
public class TransferContext {

    private JavaRDD<JiraProjectDto> projects;
    private JavaRDD<JiraIssueDto> epics;
    private JavaRDD<JiraIssueDto> issues;
    private JavaRDD<JiraWorklogDto> worklogs;
    private JavaRDD<JiraUserDto> users;

}