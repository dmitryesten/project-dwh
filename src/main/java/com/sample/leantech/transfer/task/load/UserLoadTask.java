package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.User;
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
public class UserLoadTask implements LoadTask {

    @Autowired @Qualifier("userSparkRepository")
    IRepository userRepository;

    @Override
    public void load(TransferContext ctx) {
        Collection<User> users = ctx.getDatabaseModel().getUsers();
        userRepository.save(users);
    }

}