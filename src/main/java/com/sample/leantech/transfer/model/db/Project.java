package com.sample.leantech.transfer.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project extends EntityDB {
    private Integer id;
    private Integer sid;
    private Integer logId;
    private Integer sourceId;
    private String name;
}
