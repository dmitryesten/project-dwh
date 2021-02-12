package com.sample.leantech.transfer.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Worklog extends EntityDB {
    private Integer id;
    private Integer issueId;
    private Integer logId;
    private Integer sid;
    private Integer sourceId;
    private Timestamp updated;
    private Integer timeSpentSecond;
    private String username;
    private Integer userId;
    //private String userKey;
}
