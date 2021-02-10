package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectSparkRepositoryTest {

    @Autowired
    private ProjectSparkRepository repository;

    @Test
    @DisplayName("Testing save and get methods of projectRepository")
    void testSaveGet() {
        Project project = new Project();
            project.setSid(1);
            project.setLogId(null);
            project.setSourceId(1);
            project.setName("Project-Name-Test-0. Updated 1");
        List<Project> listProject = Arrays.asList(project);

        repository.save(listProject);

        Assertions.assertNotNull(repository.get());

        Assertions.assertEquals(project.getName(),
                repository.get()
                        .stream()
                        .map(Project.class::cast)
                        .filter(objectProject -> objectProject.getName().equals(project.getName()))
                        .findFirst().get().getName(), "Name's project name values is not equals");


    }

    @Test
    public void testGroupedProject() {
        Assertions.assertNotNull(repository.getGroupedProjectMaxTimeBySourceId());
    }

    @Test
    public void testGetProjectsWithMaxTimeBySourceId() {
        Assertions.assertNotNull(repository.getProjectsWithMaxTimeBySourceId());
    }

}