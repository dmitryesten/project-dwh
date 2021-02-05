package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class UserMapperTest extends AbstractMapperTest {

    @Test
    public void test(){
        List<JiraUserDto> users = Arrays.asList(userDtoFirst(), userDtoSecond());
        TransferContext ctx = transferContext();
        List<User> listUserConverted = users
                .stream()
                .map(user -> UserMapper.INSTANCE.dtoToModel(user, ctx))
                .collect(Collectors.toList());

        Assertions.assertEquals(2, listUserConverted.size());
        Assertions.assertEquals(userDtoFirst().getKey(),
                listUserConverted.stream()
                        .filter(s -> s.getKey().equals(userDtoFirst().getKey()))
                        .findFirst().get().getKey());

    }

    private JiraUserDto userDtoFirst(){
        JiraUserDto jiraUserDtoFirst = new JiraUserDto();
        jiraUserDtoFirst.setKey("Key-Test-1");
        jiraUserDtoFirst.setName("Name-Test-1");
        return jiraUserDtoFirst;
    }

    private JiraUserDto userDtoSecond(){
        JiraUserDto jiraUserDtoFirst = new JiraUserDto();
        jiraUserDtoFirst.setKey("Key-Test-2");
        jiraUserDtoFirst.setName("Name-Test-2");
        return jiraUserDtoFirst;
    }

}