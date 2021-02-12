package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ConvertMapper.class)
public abstract class WorklogMapper {

    public static WorklogMapper INSTANCE = Mappers.getMapper(WorklogMapper.class);

    private static final String KEY_STRING_PART = "JIRAUSER";

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "issueId", source = "jiraWorklogDto.issueId"),
            @Mapping(target = "sourceId", source = "jiraWorklogDto.id"),
            @Mapping(target = "updated", source = "jiraWorklogDto.updated"),
            @Mapping(target = "timeSpentSecond", source = "jiraWorklogDto.timeSpentSeconds")
    })
    public abstract Worklog dtoToModel(JiraWorklogDto jiraWorklogDto, @Context TransferContext ctx);

    @AfterMapping
    void afterDtoToModel(JiraWorklogDto source, @MappingTarget Worklog target, @Context TransferContext ctx) {
        JiraUserDto jiraUserDto = source.getUpdateAuthor();
        if (jiraUserDto != null) {
            // Keys have "JIRAUSER10901" format
            String userId = jiraUserDto.getKey().replace(KEY_STRING_PART, "").trim();
            target.setUserId(Integer.valueOf(userId));
        }
        if (ctx != null) {
            target.setSid(ctx.getSource().getValue());
            target.setLogId(ctx.getLogInfo().getLogId());
        }
    }

}