package com.sample.leantech.transfer.model.mapper;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class ConvertMapper {

    public Timestamp map(ZonedDateTime value){
        return Timestamp.valueOf(value.toLocalDateTime());
    }

    public String map(Integer value){
        return String.valueOf(value);
    }

    public Integer map(String value){
        return Integer.valueOf(value);
    }

}