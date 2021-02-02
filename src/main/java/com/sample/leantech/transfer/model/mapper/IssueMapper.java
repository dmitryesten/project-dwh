package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

@Mapper
public interface IssueMapper extends Serializable {

    IssueMapper INSTANCE = Mappers.getMapper(IssueMapper.class);
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "sourceId", source = "jiraIssueDto.id"),
            @Mapping(target = "pid", source = "jiraIssueDto.fields.project.id"),
            @Mapping(target = "type", source = "jiraIssueDto.fields.issuetype.name"),
            @Mapping(target = "summery", source = "jiraIssueDto.fields.summary"),
            @Mapping(target = "name", source = "jiraIssueDto.key"),
            @Mapping(target = "hid",
                    source = "jiraIssueDto.fields.epic.id",
                    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
                    defaultExpression = "java(Integer.parseInt(jiraIssueDto.getFields().getParent().getId() ))") //
    })
    Issue dtoToModel(JiraIssueDto jiraIssueDto);

}
