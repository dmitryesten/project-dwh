package com.sample.leantech.transfer.model.db;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Project {
    private Long id;
    private Long sid;
    private Long logId;
    private Long sourceId;
    private String name;
}
