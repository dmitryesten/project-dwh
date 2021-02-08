package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.mapper.ProjectMapper;
import com.sample.leantech.transfer.model.mapper.WorklogMapper;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Order(6)
@Component
@RequiredArgsConstructor
public class WorklogLoadTask implements LoadTask {

    @Autowired
    @Qualifier("worklogSparkRepository")
    IRepository worklogRepository;

    @Override
    public void load(TransferContext ctx) {
        Collection<Worklog> worklogCollection = ctx.getWorklogs().stream()
                .map(worklog -> WorklogMapper.INSTANCE.dtoToModel(worklog, ctx))
                .peek(worklog -> {
                    worklog.setLogId(ctx.getLogId());
                    worklog.setSid(ctx.getSource().getValue());
                }).collect(Collectors.toList());
        worklogRepository.save(worklogCollection);
    }

}
