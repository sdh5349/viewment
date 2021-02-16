package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.user.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public String join(AccountDto userDto);
    public void validateDuplicateEmail(String email);
    public void validateDuplicateNickname(String nickname);
    public String modify(AccountDto userDto, String currentUserId);
    public void delete(String id);
}
