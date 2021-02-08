package com.sample.leantech.transfer.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogTransfer extends EntityDB {
    private Integer id;
    private Integer sid;
    private Timestamp startDt;
    private Timestamp endDt;
    private Boolean result;
}
