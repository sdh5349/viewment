package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.user.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public String join(AccountDto userDto);
    public void validateDuplicateUser(String email);

    public String modify(AccountDto userDto);
    public void delete(String id);
}
