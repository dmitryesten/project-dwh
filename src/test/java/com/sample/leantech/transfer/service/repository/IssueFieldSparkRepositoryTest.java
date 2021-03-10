package com.sample.leantech.transfer.service.repository;

import com.sample.leantech.transfer.model.db.IssueField;
import com.sample.leantech.transfer.model.db.NameTypeField;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
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
            issueField.setLogId(5622);
            issueField.setIssueSourceId(10594);
            issueField.setField("Filed-Test");
            issueField.setType(NameTypeField.CUSTOMER.getNameType());
            issueField.setName("Name-Test3");
            issueField.setValue("value-test4");

        issueFieldSparkRepository.save(Arrays.asList(issueField));
        //issueFieldSparkRepository.getDataset().show();
        assertThat(issueFieldSparkRepository.getDataset().isEmpty()).isFalse();

    }

    @Test
    public void testGetIssueWithMaxLogIdBySourceId() {
        List<Integer> listSourceIdIssues = Arrays.asList(10594, 10593);
        Dataset<IssueField> datasetFilteredIssues =
                issueFieldSparkRepository.getIssueFieldWithMaxLogIdBySourceId(listSourceIdIssues);
        datasetFilteredIssues.show();
        Assertions.assertEquals(listSourceIdIssues.size(), datasetFilteredIssues.count());

    }

}