package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.user.UserDto;
import com.web.curation.exceptions.UserDuplicateException;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class AccountServiceImpl implements AccountService{

    private final UserRepository userRepository;

    @Override
    public String join(UserDto userDto) {
        validateDuplicateUser(userDto.getEmail());

        //ToDo modelMapper 적용
        User newUser = userDto.convert();

        userRepository.save(newUser);
        return newUser.getId();
    }

    @Override
    public void validateDuplicateUser(String email) {
        userRepository.findByEmail(email)
                .ifPresent(m-> {throw new UserDuplicateException(email);}
        );
    }

    @Override
    public String modify(UserDto userDto) {
        User user = userRepository.findById(userDto.getUserId())
                .orElseThrow(
                        ()->{throw new UsernameNotFoundException(userDto.getEmail() + "은 등록되지 않은 사용자입니다.");});
        userRepository.update(userDto.convert(user));
        return userDto.getUserId();
    }

    @Override
    public String delete(String id) {
        userRepository.delete(id);
        return id;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findOne(String id) {
        return userRepository.findById(id);
    }
}
