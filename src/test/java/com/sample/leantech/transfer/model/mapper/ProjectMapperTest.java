package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.Project;
import com.sample.leantech.transfer.model.dto.request.JiraProjectDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProjectMapperTest extends AbstractMapperTest {

    @Test
    public void testDtoToModel() {
        JiraProjectDto projectDto = jiraProjectDto();
        TransferContext ctx = transferContext();

        Project project = ProjectMapper.INSTANCE.dtoToModel(projectDto, ctx);

        assertThat(project.getId()).isNull();
        assertThat(project.getSid()).isEqualTo(ctx.getSource().getValue());
        assertThat(project.getLogId()).isEqualTo(ctx.getLogInfo().getLogId());
        assertThat(project.getSourceId()).isEqualTo(Integer.valueOf(projectDto.getId()));
        assertThat(project.getName()).isEqualTo(projectDto.getName());
    }

}