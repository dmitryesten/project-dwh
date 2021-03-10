package com.sample.leantech.transfer.parser;

import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.service.repository.ProjectSparkRepository;
import lombok.val;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class SparkParserJsonProjectTest {

    @Autowired
    private SparkSession sparkSession;

    @Test
    public void testParsedJsonProjects(){
        List<String> json = Arrays.asList(JIRA_JSON_PROJECTS_EXAMPLE);
        Dataset<String> datasetStringProject = sparkSession.createDataset(json, Encoders.STRING());
        Dataset<Row> parsedProjects = sparkSession.read().json(JIRA_JSON_PROJECTS_EXAMPLE);
        parsedProjects.show();
        parsedProjects.select("id", "key", "name", "projectTypeKey").show();
    }

    private static String JIRA_JSON_PROJECTS_EXAMPLE = "" +
            "[\n" +
            "    {\n" +
            "        \"expand\": \"description,lead,url,projectKeys\",\n" +
            "        \"self\": \"https://jira.ourcode.ru/rest/api/2/project/10200\",\n" +
            "        \"id\": \"10200\",\n" +
            "        \"key\": \"DEM\",\n" +
            "        \"name\": \"DEMOP\",\n" +
            "        \"avatarUrls\": {\n" +
            "            \"48x48\": \"https://jira.ourcode.ru/secure/projectavatar?avatarId=10324\",\n" +
            "            \"24x24\": \"https://jira.ourcode.ru/secure/projectavatar?size=small&avatarId=10324\",\n" +
            "            \"16x16\": \"https://jira.ourcode.ru/secure/projectavatar?size=xsmall&avatarId=10324\",\n" +
            "            \"32x32\": \"https://jira.ourcode.ru/secure/projectavatar?size=medium&avatarId=10324\"\n" +
            "        },\n" +
            "        \"projectTypeKey\": \"software\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"expand\": \"description,lead,url,projectKeys\",\n" +
            "        \"self\": \"https://jira.ourcode.ru/rest/api/2/project/10201\",\n" +
            "        \"id\": \"10201\",\n" +
            "        \"key\": \"MTAS\",\n" +
            "        \"name\": \"MTASK\",\n" +
            "        \"avatarUrls\": {\n" +
            "            \"48x48\": \"https://jira.ourcode.ru/secure/projectavatar?avatarId=10324\",\n" +
            "            \"24x24\": \"https://jira.ourcode.ru/secure/projectavatar?size=small&avatarId=10324\",\n" +
            "            \"16x16\": \"https://jira.ourcode.ru/secure/projectavatar?size=xsmall&avatarId=10324\",\n" +
            "            \"32x32\": \"https://jira.ourcode.ru/secure/projectavatar?size=medium&avatarId=10324\"\n" +
            "        },\n" +
            "        \"projectTypeKey\": \"software\"\n" +
            "    }\n" +
            "]";

}
