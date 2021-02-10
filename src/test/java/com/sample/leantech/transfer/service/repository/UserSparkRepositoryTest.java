package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.User;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
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

        List<User> listUser = Arrays.asList(user);
        Dataset<User> listOfJira = sparkSession.createDataset(listUser, Encoders.bean(User.class));
        listOfJira.show();
        Dataset<User> datasetOfDb =
                sparkSession.createDataset((List<User>) repository.get(), Encoders.bean(User.class));
        datasetOfDb.show();

        listOfJira.join(datasetOfDb, "key").show();

        //datasetOfJira.toDF().show();


        //repository.save(listUser);

       /* Assertions.assertNotNull(repository.get());

        Assertions.assertEquals(user.getName(),
                repository.get().stream()
                        .map(User.class::cast)
                        .filter(objectUser -> objectUser.getName().equals(user.getName()))
                        .findFirst().get()
                        .getName(), "Name's user values is not equals");
       */


    }

}