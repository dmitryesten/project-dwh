package com.sample.leantech.transfer.task.load;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.mapper.UserMapper;
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
@Order(5)
@Component
@RequiredArgsConstructor
public class UserLoadTask implements LoadTask {

    @Autowired @Qualifier("userSparkRepository")
    IRepository userRepository;

    @Override
    public void load(TransferContext ctx) {
        Collection<User> userCollection = ctx.getUsers().stream()
                .map(user -> UserMapper.INSTANCE.dtoToModel(user, ctx))
                .collect(Collectors.toList());
        userRepository.save(userCollection);
    }

}
