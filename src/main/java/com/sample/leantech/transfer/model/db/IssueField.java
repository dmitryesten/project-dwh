package com.sample.leantech.transfer.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueField extends EntityDB {

    private Integer id;
    private Integer issueId;
    private Integer sid;
    private Integer logId;
    private Integer issueSourceId;
    private String field;
    private String type;
    private String name;
    private String value;
    private Timestamp createDt;

}
