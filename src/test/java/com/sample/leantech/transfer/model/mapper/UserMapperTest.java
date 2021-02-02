package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@SpringBootTest
class UserMapperTest {

    @Autowired
    private JavaSparkContext javaSparkCtx;

    @Test
    public void test(){
        List<JiraUserDto> listJiraUserDto = Arrays.asList(getUserDtoFirst(), getUserDtoSecond());
        JavaRDD<JiraUserDto> javaRddUsers = javaSparkCtx.parallelize(listJiraUserDto);

        Collection<User> listUserConvertedRdd = javaRddUsers.map(UserMapper.INSTANCE::dtoToModel).collect();

        Assertions.assertEquals(2, listUserConvertedRdd.size());
        Assertions.assertEquals(getUserDtoFirst().getKey(),
                listUserConvertedRdd.stream()
                        .filter(s -> s.getKey().equals(getUserDtoFirst().getKey()))
                        .findFirst().get().getKey());

    }

    private JiraUserDto getUserDtoFirst(){
        JiraUserDto jiraUserDtoFirst = new JiraUserDto();
        jiraUserDtoFirst.setSelf("Test-self_test-1");
        jiraUserDtoFirst.setKey("Key-Test-1");
        jiraUserDtoFirst.setName("Name-Test-1");
        jiraUserDtoFirst.setEmailAddress("test1@mail.ru");
        jiraUserDtoFirst.setDisplayName("DisplayName");
        jiraUserDtoFirst.setActive(true);
        jiraUserDtoFirst.setDeleted(false);
        jiraUserDtoFirst.setTimeZone("6+");
        jiraUserDtoFirst.setLocale("RU");
        return jiraUserDtoFirst;
    }

    private JiraUserDto getUserDtoSecond(){
        JiraUserDto jiraUserDtoFirst = new JiraUserDto();
        jiraUserDtoFirst.setSelf("Test-self_test-2");
        jiraUserDtoFirst.setKey("Key-Test-2");
        jiraUserDtoFirst.setName("Name-Test-2");
        jiraUserDtoFirst.setEmailAddress("test2@mail.ru");
        jiraUserDtoFirst.setDisplayName("DisplayName2");
        jiraUserDtoFirst.setActive(true);
        jiraUserDtoFirst.setDeleted(false);
        jiraUserDtoFirst.setTimeZone("6+");
        jiraUserDtoFirst.setLocale("RU");
        return jiraUserDtoFirst;
    }

}