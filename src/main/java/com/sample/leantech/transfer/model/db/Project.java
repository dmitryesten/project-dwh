package com.sample.leantech.transfer.model.db;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Project {
    private long id;
    private long sid;
    private long logId;
    private long sourceId;
    private String name;
}
