package com.sample.leantech.transfer.parser;

import com.sample.leantech.transfer.integration.JiraClient;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraIssueResponseDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.apache.spark.sql.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.apache.spark.sql.functions.*;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.explode;

@SpringBootTest
public class SparkParserJsonIssuesTest {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private JiraClient jiraClient;

    @Autowired
    private RestTemplate jiraRestTemplate;

    @Test
    public void testParsedJsonProjects() {
        JiraIssueDto jiraIssueDto = new JiraIssueDto();
        List<String> issues = Arrays.asList(
                jiraRestTemplate.exchange("/rest/agile/1.0/epic/none/issue", HttpMethod.GET, null, String.class).getBody(),
                jiraRestTemplate.exchange("/rest/api/2/search?jql=issuetype=\"Epic\"", HttpMethod.GET, null, String.class).getBody()
        );
        Dataset<String> datasetStringIssues = sparkSession.createDataset(issues, Encoders.STRING());
        Dataset<Row> parsedIssuesNoEpic = sparkSession.read().json(datasetStringIssues);
        parsedIssuesNoEpic.select(
                explode(col("issues")).as("issues")
        ).select(col("issues.id"),
                col("issues.key"),
                col("issues.fields.issuetype.name"),
                col("issues.fields.summary"),
                col("issues.fields.parent.id").as("parent_id")
        ).orderBy(col("issues.id")).show(false);
    }

}
