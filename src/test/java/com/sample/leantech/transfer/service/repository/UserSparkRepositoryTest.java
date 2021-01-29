package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class UserSparkRepositoryTest {

    @Autowired
    private UserSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of userRepository")
    public void saveGetTest() {
        User user = new User();
            user.setKey("Test-key-0");
            user.setLogId(null);
            user.setName(UUID.randomUUID().toString());
        List<User> listUser = Arrays.asList(user);

        repository.save(listUser);

        Assertions.assertNotNull(repository.getUsers());

        Assertions.assertEquals(user.getName(),
                repository.getUsers().stream()
                        .filter(objectUser -> objectUser.getName().equals(user.getName()))
                        .findFirst().get()
                        .getName(), "Name's user values is not equals");

    }

}