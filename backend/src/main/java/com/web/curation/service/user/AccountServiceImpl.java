package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.UserDto;
import com.web.curation.exceptions.UserDuplicateException;
import com.web.curation.repository.UserRepository;
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
        validateDuplicateUser(userDto);

        //ToDo modelMapper 적용
        User newUser = userDto.convert();

        userRepository.save(newUser);
        return newUser.getId();
    }

    @Override
    public void validateDuplicateUser(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(m-> {throw new UserDuplicateException(userDto.getEmail());}
        );
    }

    @Override
    public String modify(UserDto userDto) {
        User user = userRepository.findById(userDto.getUid())
                .orElseThrow(
                        ()->{throw new UsernameNotFoundException(userDto.getEmail() + "은 등록되지 않은 사용자입니다.");});
        userRepository.update(userDto.convert(user));
        return userDto.getUid();
    }

    @Override
    public String delete(String id) {
        userRepository.delete(id);
        return id;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findOne(String id) {
        return userRepository.findById(id);
    }
}
