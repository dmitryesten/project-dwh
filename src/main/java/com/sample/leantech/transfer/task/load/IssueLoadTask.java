package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Order(4)
@Component
@RequiredArgsConstructor
public class IssueLoadTask implements LoadTask  {

    @Autowired
    @Qualifier("issueSparkRepository")
    IRepository issueSparkRepository;


    public void load(TransferContext ctx) {
        Collection<Issue> issues = ctx.getDatabaseModel().getIssues();
        issueSparkRepository.save(issues);
    }

}