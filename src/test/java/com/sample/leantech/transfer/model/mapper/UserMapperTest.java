package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.Source;
import com.sample.leantech.transfer.model.context.TransferContext;
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
        List<JiraUserDto> listJiraUserDto = Arrays.asList(userDtoFirst(), userDtoSecond());
        JavaRDD<JiraUserDto> javaRddUsers = javaSparkCtx.parallelize(listJiraUserDto);
        TransferContext ctx = transferContext();
        Collection<User> listUserConvertedRdd = javaRddUsers.map(user ->
                UserMapper.INSTANCE.dtoToModel(user, ctx)).collect();

        Assertions.assertEquals(2, listUserConvertedRdd.size());
        Assertions.assertEquals(userDtoFirst().getKey(),
                listUserConvertedRdd.stream()
                        .filter(s -> s.getKey().equals(userDtoFirst().getKey()))
                        .findFirst().get().getKey());

    }

    private JiraUserDto userDtoFirst(){
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

    private JiraUserDto userDtoSecond(){
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

    private TransferContext transferContext() {
        TransferContext ctx = new TransferContext();
        ctx.setSource(Source.JIRA_1);
        ctx.setLogId(1);
        return ctx;
    }

}