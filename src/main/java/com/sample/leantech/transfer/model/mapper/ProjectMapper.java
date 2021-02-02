package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

@Mapper
public interface ProjectMapper extends Serializable {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    Project dtoToModel(JiraProjectDto jiraProjectDto);

}
