package com.sample.leantech.transfer.controller.jira;

import com.sample.leantech.transfer.model.db.Source;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.*;

@RestController
@RequestMapping("/source")
@RequiredArgsConstructor
public class SourceController {

    private final SparkSession sparkSession;

    private final DataSource dataSource;

    private final JiraProjectService jiraProjectService;

    @GetMapping("/get")
    public String get(){
        Properties property = new Properties();
        property.setProperty("driver", "org.postgresql.Driver");
        property.setProperty("url", "jdbc:postgresql://localhost:5432/project_dwh");
        property.setProperty("user", "spring");
        property.setProperty("password", "123");


        Dataset<JiraProjectDto> dataSet = sparkSession.createDataset(jiraProjectService.getProjects(), Encoders.bean(JiraProjectDto.class));
        dataSet.show();

        //Insert to database
        List<Source> listSource =
                Arrays.asList(new Source(new Random().nextInt(), "Jira"), new Source(new Random().nextInt(), "Redmine"));
        Dataset<Source> insertSource =  sparkSession.createDataset(listSource, Encoders.bean(Source.class));
        insertSource.write().mode(SaveMode.Append).jdbc(property.getProperty("url"), "sources", property);

        //Select all rows of sources database's table
        sparkSession.read().jdbc(property.getProperty("url"), "sources", property).show();
        Dataset<Row> rowTable = sparkSession.read().jdbc(property.getProperty("url"), "sources", property).toDF();
        return rowTable.toString();
    }

}
