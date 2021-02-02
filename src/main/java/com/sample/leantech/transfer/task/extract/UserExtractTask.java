package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import lombok.RequiredArgsConstructor;
import org.apache.spark.api.java.JavaRDD;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 6)
@RequiredArgsConstructor
public class UserExtractTask implements ExtractTask {

    @Override
    public void extract(TransferContext ctx) {
        JavaRDD<JiraUserDto> users = ctx.getWorklogs()
                .map(JiraWorklogDto::getUpdateAuthor)
                .distinct();
        ctx.setUsers(users);
    }

}