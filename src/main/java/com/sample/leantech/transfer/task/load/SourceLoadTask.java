package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Source;
import com.sample.leantech.transfer.service.repository.IRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor

public class SourceLoadTask implements LoadTask {

    @Autowired @Qualifier("sourceSparkRepository")
    private IRepository sourceRepository;

    @Override
    public void load(TransferContext ctx) {

    }
}
