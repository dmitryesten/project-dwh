package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

@Mapper
public interface UserMapper extends Serializable {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User dtoToModel(JiraUserDto jiraUserDto);

}
