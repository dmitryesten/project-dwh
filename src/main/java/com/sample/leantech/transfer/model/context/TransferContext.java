package com.sample.leantech.transfer.model.context;

import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.spark.rdd.RDD;

@Data
@NoArgsConstructor
public class TransferContext {

    private RDD<JiraProjectDto> projects;
    private RDD<JiraUserDto> users;
    // TODO: add issues and worklows

}