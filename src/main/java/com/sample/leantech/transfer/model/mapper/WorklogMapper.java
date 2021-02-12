package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ConvertMapper.class)
public interface WorklogMapper {

    WorklogMapper INSTANCE = Mappers.getMapper(WorklogMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "issueId", source = "jiraWorklogDto.issueId"),
            @Mapping(target = "sourceId", source = "jiraWorklogDto.id"),
            @Mapping(target = "updated", source = "jiraWorklogDto.updated"),
            @Mapping(target = "timeSpentSecond", source = "jiraWorklogDto.timeSpentSeconds")
    })
    Worklog dtoToModel(JiraWorklogDto jiraWorklogDto, @Context TransferContext ctx);

    @AfterMapping
    default void afterDtoToModel(JiraWorklogDto source, @MappingTarget Worklog target, @Context TransferContext ctx) {
        JiraUserDto jiraUserDto = source.getUpdateAuthor();
        if (jiraUserDto != null) {
            // TODO: keys have non-Integer values ("JIRAUSER10901" format)
            // TODO: change column type or remove non-Integer part of key
            target.setUserId(Integer.valueOf(jiraUserDto.getKey()));
        }
        if (ctx != null) {
            target.setSid(ctx.getSource().getValue());
            target.setLogId(ctx.getLogInfo().getLogId());
        }
    }

}