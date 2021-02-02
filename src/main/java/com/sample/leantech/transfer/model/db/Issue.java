package com.sample.leantech.transfer.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue extends EntityDB {
    private Integer id;
    private Integer pid;
    private Integer sid;
    private Integer logId;
    private Integer hid;
    private Integer sourceId;
    private String type;
    private String name;
    private String summery;
}
