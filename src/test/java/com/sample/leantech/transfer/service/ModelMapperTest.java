package com.sample.leantech.transfer.service;

import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
public class ModelMapperTest {

    @Autowired
    private JavaSparkContext javaSparkCtx;

    @Test
    public void test(){
        List<JiraUserDto> listJiraUserDto = Arrays.asList(getUserDtoFirst(), getUserDtoSecond());
        ModelMapper modelMapper = new ModelMapper();

        List<User> listUser = listJiraUserDto.stream().map(s -> modelMapper.map(s, User.class)).collect(Collectors.toCollection(ArrayList::new));
        Assertions.assertEquals(2, listUser.size());

        JavaRDD<JiraUserDto> javaRddUsers = javaSparkCtx.parallelize(listJiraUserDto);
        List<User> listUserConvertedRdd = javaRddUsers.map(dto -> {
            User user = new User();
            user.setName(dto.getName());
            user.setLogId(null);
            user.setKey(UUID.randomUUID().toString());
            return user;
        }).collect();
        Assertions.assertEquals(2, listUserConvertedRdd.size());
        listUserConvertedRdd.forEach(s -> System.out.println(s));
        //НЕ РАБОТАЕТList<User> listUser2 = javaRddUsers.map(s -> modelMapper.map(s, User.class)).collect();

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
