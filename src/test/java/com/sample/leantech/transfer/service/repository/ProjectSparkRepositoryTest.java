package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectSparkRepositoryTest {

    @Autowired
    private ProjectSparkRepository repository;

    @Test
    void getProjects() {
        Project project = new Project();
            project.setId(Long.valueOf(new Random().nextInt()));
            project.setSid(1L);
            project.setLogId(null);
            project.setSourceId(101L);
        List<Project> listProject = Arrays.asList(project);

        repository.save(listProject);

        Assertions.assertNotNull(repository.getProjects());
        Assertions.assertTrue(repository.getProjects().contains(project));

    }

    @Test
    void save() {
        Project project = new Project();
            project.setId(Long.valueOf(new Random().nextInt()));
            project.setSid(1L);
            project.setLogId(null);
            project.setSourceId(101L);
        List<Project> listProject = Arrays.asList(project);

        repository.save(listProject);

        Assertions.assertEquals(
                project.getId(),
                repository.getProjects()
                        .stream()
                        .filter(s -> s.getId().equals(project.getId()))
                        .findAny().get().getId()
        );

    }
}