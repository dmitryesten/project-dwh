package com.sample.leantech.transfer.task.extract;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(value = 6)
@RequiredArgsConstructor
public class UserExtractTask implements ExtractTask {

    @Override
    public void extract(TransferContext ctx) {
        List<JiraUserDto> users = ctx.getWorklogs()
                .stream()
                .map(JiraWorklogDto::getUpdateAuthor)
                .distinct()
                .collect(Collectors.toList());
        ctx.setUsers(users);
    }

}