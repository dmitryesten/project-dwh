package com.sample.leantech.transfer.model.mapper;

import scala.Int;
import scala.Serializable;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class ConvertMapper implements Serializable {

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
