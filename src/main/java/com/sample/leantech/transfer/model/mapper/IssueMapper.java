package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.JiraResult;
import com.sample.leantech.transfer.model.db.Issue;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Objects;
import java.util.stream.Stream;

@Mapper
public interface IssueMapper {

    IssueMapper INSTANCE = Mappers.getMapper(IssueMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "pid", source = "jiraIssueDto.fields.project.id"),
            @Mapping(target = "sourceId", source = "jiraIssueDto.id"),
            @Mapping(target = "type", source = "jiraIssueDto.fields.issuetype.name"),
            @Mapping(target = "name", source = "jiraIssueDto.key"),
            @Mapping(target = "summery", source = "jiraIssueDto.fields.summary")
    })
    Issue dtoToModel(JiraIssueDto jiraIssueDto, @Context JiraResult jiraResult);

    @AfterMapping
    default void afterDtoToModel(JiraIssueDto source, @MappingTarget Issue target, @Context JiraResult ctx) {
        JiraIssueDto.Fields fields = source.getFields();
        if (fields != null) {
            Stream.of(fields.getParent(), fields.getEpic())
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(JiraIssueDto.Fields.Parent::getId)
                    .map(Integer::valueOf)
                    .ifPresent(target::setHid);
        }
        if (ctx != null) {
            target.setSid(ctx.getSource().getValue());
            target.setLogId(ctx.getParentLogId());
        }
    }

}