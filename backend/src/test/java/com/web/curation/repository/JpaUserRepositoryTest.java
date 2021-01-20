package com.web.curation.repository;

import com.web.curation.domain.User;
import com.web.curation.domain.UserRole;
import com.web.curation.dto.UserDto;
import com.web.curation.service.user.AccountService;
import com.web.curation.service.user.AccountServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class JpaUserRepositoryTest {

    @Autowired AccountService accountService;

    @Test
    void user_join_test() {
        UserDto dto = new UserDto();
        dto.setId("1");
        dto.setEmail("aaaa");
        dto.setNickname("bbbb");
        dto.setRole(UserRole.USER);

        accountService.join(dto);

        User findUser = accountService.findOne(dto.getId()).get();
        Assertions.assertThat(dto.getEmail()).isEqualTo(findUser.getEmail());
    }

    @Test
    void user_modify_test() {
        UserDto dto = new UserDto();
        dto.setId("1");
        dto.setEmail("aaaa");
        dto.setNickname("bbbb");
        dto.setRole(UserRole.USER);

        accountService.join(dto);

        dto.setNickname("cccc");

        String uid = accountService.modify(dto);
        User modeUser = accountService.findOne(dto.getId()).get();
        Assertions.assertThat(modeUser.getNickname()).isEqualTo("cccc");
    }

}
