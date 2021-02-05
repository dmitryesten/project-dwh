package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraResult;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

@Mapper(uses = ConvertMapper.class)
public interface ProjectMapper extends Serializable {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "sourceId", source = "jiraProjectDto.id"),
            @Mapping(target = "name", source = "jiraProjectDto.name")
    })
    Project dtoToModel(JiraProjectDto jiraProjectDto, @Context JiraResult jiraResult);

    @AfterMapping
    default void afterDtoToModel(JiraProjectDto source, @MappingTarget Project target, @Context JiraResult ctx) {
        if (ctx != null) {
            target.setSid(ctx.getSource().getValue());
            target.setLogId(ctx.getLogId());
        }
    }

}
