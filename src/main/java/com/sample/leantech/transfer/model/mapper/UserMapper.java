package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ConvertMapper.class)
public abstract class UserMapper {

    public static UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    private static final String USER_KEY_STRING_PART_REGEX = "[^\\d]";

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "key", ignore = true),
            @Mapping(target = "name", source = "jiraUserDto.name")
    })
    public abstract User dtoToModel(JiraUserDto jiraUserDto, @Context TransferContext ctx);

    @AfterMapping
    void afterDtoToModel(JiraUserDto source, @MappingTarget User target, @Context TransferContext ctx) {
        target.setKey(truncatedUserKey(source.getKey()));
        target.setName(source.getName());
        if (ctx != null) {
            target.setLogId(ctx.getLogInfo().getLogId());
        }
    }

    String truncatedUserKey(String userKey) {
        // Keys have "JIRAUSER10001" format
        return userKey.replaceAll(USER_KEY_STRING_PART_REGEX, "");
    }

}