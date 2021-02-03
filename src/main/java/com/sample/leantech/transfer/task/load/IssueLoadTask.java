package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.mapper.IssueMapper;
import com.sample.leantech.transfer.model.mapper.ProjectMapper;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

//@Slf4j
//@Order(4)
@Component
@RequiredArgsConstructor
public class IssueLoadTask {

    @Autowired
    @Qualifier("issueSparkRepository")
    IRepository issueSparkRepository;


    public void load(TransferContext ctx) {
        Collection<Issue> projectCollection =
                ctx.getIssues().stream()
                .map(issue -> IssueMapper.INSTANCE.dtoToModel(issue, ctx))
                .collect(Collectors.toList());

        issueSparkRepository.save(projectCollection);
    }

}
