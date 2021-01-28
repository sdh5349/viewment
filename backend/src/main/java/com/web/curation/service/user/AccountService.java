package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.user.UserDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public String join(UserDto userDto);
    public void validateDuplicateUser(String email);

    public String modify(UserDto userDto);
    public String delete(String id);
    public List<User> findAll();
    public Optional<User> findOne(String id);
}
