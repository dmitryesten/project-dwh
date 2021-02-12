package com.sample.leantech.transfer.model.mapper;

import com.sample.leantech.transfer.model.context.TransferContext;
import com.sample.leantech.transfer.model.db.User;
import com.sample.leantech.transfer.model.dto.request.JiraUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserMapperTest extends AbstractMapperTest {

    @Test
    public void testDtoToModel() {
        JiraUserDto userDto = jiraUserDto();
        TransferContext ctx = transferContext();

        User user = UserMapper.INSTANCE.dtoToModel(userDto, ctx);

        assertThat(user.getId()).isNull();
        assertThat(user.getKey()).isEqualTo(USER_ID);
        assertThat(user.getLogId()).isEqualTo(ctx.getLogInfo().getLogId());
        assertThat(user.getName()).isEqualTo(userDto.getName());
    }

}