package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectSparkRepositoryTest {

    @Autowired
    private ProjectSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of projectRepository")
    void getProjects() {
        Project project = new Project();
            project.setSid(1);
            project.setLogId(null);
            project.setSourceId(101);
            project.setName(UUID.randomUUID().toString());
        List<Project> listProject = Arrays.asList(project);

        repository.save(listProject);

        Assertions.assertNotNull(repository.getProjects());

        Assertions.assertEquals(project.getName(),
                repository.getProjects()
                        .stream()
                        .filter(objectProject -> objectProject.getName().equals(project.getName()))
                        .findFirst().get().getName(), "Name's project name values is not equals");
    }

}