package com.sample.leantech.transfer.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends EntityDB{
    private Integer id;
    private String key;
    private Integer logId;
    private String name;
}
