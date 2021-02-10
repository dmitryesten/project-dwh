package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ConvertMapper.class)
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "sourceId", source = "jiraProjectDto.id"),
            @Mapping(target = "name", source = "jiraProjectDto.name")
    })
    Project dtoToModel(JiraProjectDto jiraProjectDto, @Context TransferContext ctx);

    @AfterMapping
    default void afterDtoToModel(JiraProjectDto source, @MappingTarget Project target, @Context TransferContext ctx) {
        if (ctx != null) {
            target.setSid(ctx.getSource().getValue());
            target.setLogId(ctx.getLogInfo().getLogId());
        }
    }

}
