package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.Project;
import org.apache.spark.sql.Dataset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ProjectSparkRepositoryTest extends AbstractRepositoryTest {

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
        Assertions.assertNotNull(repository.getGroupedProjectMaxLogIdBySourceId());
    }

    @Test
    public void testGetProjectsWithMaxTimeBySourceId() {
        List<Integer> listIdProjectJira = Arrays.asList(10200, 10201);
        Dataset<Project> datasetProject = repository.getProjectsWithMaxLogIdBySourceId(listIdProjectJira);
        datasetProject.show();
        Assertions.assertNotNull(datasetProject);
        Assertions.assertEquals(listIdProjectJira.size(), datasetProject.count());
    }

}