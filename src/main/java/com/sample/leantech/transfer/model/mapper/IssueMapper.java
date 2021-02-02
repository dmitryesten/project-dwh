package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

@Mapper
public interface IssueMapper extends Serializable {

    IssueMapper INSTANCE = Mappers.getMapper(IssueMapper.class);

    Issue dtoToModel(JiraIssueDto jiraIssueDto);

}
