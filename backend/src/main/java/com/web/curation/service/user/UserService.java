package com.web.curation.service.user;

import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.dto.user.UserPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    public UserPageDto findUserPageById(String currentUserId, String userId);
    public SimpleUserInfoDto findUserByEmail(String currentUserId, String email);
    public Page<SimpleUserInfoDto> findUsersByNickname(String currentUserId, String nickname, Pageable pageable);
}
