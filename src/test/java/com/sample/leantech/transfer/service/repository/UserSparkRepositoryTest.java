package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.User;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class UserSparkRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private UserSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of userRepository")
    public void testSaveGet() {
        User user = new User();
            user.setKey(UUID.randomUUID().toString());
            user.setLogId(null);
            user.setName("Name-Key-000");

        repository.save(Arrays.asList(user));

        Assertions.assertNotNull(repository.get());

        Dataset<User> datasetOfDb =
                sparkSession.createDataset((List<User>) repository.get(), Encoders.bean(User.class));

        Assertions.assertEquals(user.getName(),
                repository.get().stream()
                        .map(User.class::cast)
                        .filter(objectUser -> objectUser.getName().equals(user.getName()))
                        .findFirst().get()
                        .getName(), "Name's user values is not equals");

    }

    @Test
    public void testGroupedUsers() {
        repository.getGroupedUserMaxLogIdByKey().show();
        Assertions.assertNotNull(repository.getGroupedUserMaxLogIdByKey());
    }

    @Test
    public void testGetUsersWithJoinedByMaxLogid() {
        repository.getUserWithMaxLogIdByKey().show();
        Assertions.assertNotNull(repository.getGroupedUserMaxLogIdByKey());
    }

    @Test
    public void testGetUserById() {
        Row row = repository.getUserByKey("JIRAUSER10909");
        Assertions.assertNotNull(row.get(0));
    }

}