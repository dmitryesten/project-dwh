package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.IssueField;
import com.sample.leantech.transfer.model.dto.request.JiraIssueDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(uses = ConvertMapper.class)
public interface IssueFieldMapper {

    IssueFieldMapper INSTANCE = Mappers.getMapper(IssueFieldMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "issueId", ignore = true, source = "jiraIssueDto.id"),
            @Mapping(target = "issueSourceId", source = "jiraIssueDto.id"),
            @Mapping(target = "value", source = "jiraIssueDto.fields.customfield.value")
    })
    IssueField dtoToModel(JiraIssueDto jiraIssueDto, @Context TransferContext ctx);

    @AfterMapping
    default void afterDtoToModel(JiraIssueDto source, @MappingTarget IssueField target, @Context TransferContext ctx) {
        if (ctx != null) {
            target.setSid(ctx.getSource().getValue());
            target.setLogId(ctx.getLogInfo().getLogId());
            try {
                target.setField(JiraIssueDto.Fields.class.getDeclaredField("customfield").getName());
                target.setType(JiraIssueDto.Fields.Customfield.class.getDeclaredField("value").getGenericType().getTypeName());
            } catch (NoSuchFieldException e) {
                target.setField(null);
                target.setType(null);
            }
        }
    }

}
