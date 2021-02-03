package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.Worklog;
import com.sample.leantech.transfer.model.dto.request.JiraWorklogDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ConvertMapper.class)
public interface WorklogMapper {

    WorklogMapper INSTANCE = Mappers.getMapper(WorklogMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "sourceId", source = "jiraWorklogDto.id"),
            @Mapping(target = "timeSpentSecond", source = "jiraWorklogDto.timeSpentSeconds")
    })
    Worklog dtoToModel(JiraWorklogDto jiraWorklogDto);
}
