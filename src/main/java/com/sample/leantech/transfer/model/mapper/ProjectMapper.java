package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
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
    Project dtoToModel(JiraProjectDto jiraProjectDto);

}
