package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

@Mapper
public interface UserMapper extends Serializable {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User dtoToModel(JiraUserDto jiraUserDto);

    @AfterMapping
    default void afterDtoToModel(JiraUserDto source, @MappingTarget User target, @Context TransferContext ctx) {
        if (ctx != null) {
            target.setLogId(ctx.getLogId());
        }
    }

}
