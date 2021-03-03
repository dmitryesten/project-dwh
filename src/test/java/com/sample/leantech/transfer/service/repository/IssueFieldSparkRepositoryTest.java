package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.IssueField;
import org.apache.spark.sql.Dataset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class IssueFieldSparkRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private IssueFieldSparkRepository issueFieldSparkRepository;

    @Test
    public void testGetDataset(){
        issueFieldSparkRepository.getDataset().show();
        assertThat(issueFieldSparkRepository.getDataset()).isNotNull();
    }

    @Test
    public void testSave() {
        IssueField issueField = new IssueField();
            issueField.setSid(1);
            issueField.setLogId(123);
            issueField.setIssueSourceId(10594);
            issueField.setField("fieldTest3");
            issueField.setType("typeTestString3");
            issueField.setName("Name-Test3");
            issueField.setValue("value-test3");

        issueFieldSparkRepository.save(Arrays.asList(issueField));
        issueFieldSparkRepository.getDataset().show();
        assertThat(issueFieldSparkRepository.getDataset().isEmpty()).isFalse();
    }

    @Test
    public void testGetIssueWithMaxLogIdBySourceId(){
        List<Integer> listSourceIdIssues = Arrays.asList(10594, 10780);
        Dataset<IssueField> datasetFilteredIssues =
                issueFieldSparkRepository.getIssueFieldWithMaxLogIdBySourceId(listSourceIdIssues);
        datasetFilteredIssues.show();
        Assertions.assertEquals(listSourceIdIssues.size(), datasetFilteredIssues.count());

    }

}