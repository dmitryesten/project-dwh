package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

@Mapper
public interface UserMapper extends Serializable {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "key", source = "jiraUserDto.key"),
            @Mapping(target = "name", source = "jiraUserDto.name")
    })
    User dtoToModel(JiraUserDto jiraUserDto, @Context TransferContext ctx);

    @AfterMapping
    default void afterDtoToModel(JiraUserDto source, @MappingTarget User target, @Context TransferContext ctx) {
        if (ctx != null) {
            target.setLogId(ctx.getLogId());
        }
    }

}