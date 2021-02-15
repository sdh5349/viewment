package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.domain.UserRole;
import com.web.curation.dto.user.AccountDto;
import com.web.curation.exceptions.UserDuplicateException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class AccountServiceImpl implements AccountService{

    private final UserRepository userRepository;

    private User getUser(String userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()->{throw new UserNotFoundException();}
        );
        return user;
    }

    @Override
    public String join(AccountDto userDto) {
        validateDuplicateEmail(userDto.getEmail());
        validateDuplicateNickname(userDto.getNickname());

        //ToDo modelMapper 적용
        User newUser = new User();
        newUser.setId(userDto.getUserId());
        newUser.setEmail(userDto.getEmail());
        newUser.setNickname(userDto.getNickname());
        newUser.setRole(UserRole.USER);
        newUser.setMessage(userDto.getMessage());

        userRepository.save(newUser);
        return newUser.getId();
    }

    @Override
    public void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(m-> {throw new UserDuplicateException(email, null);}
        );
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(m-> {throw new UserDuplicateException(null, nickname);}
                );
    }

    @Override
    public String modify(AccountDto userDto) {
        validateDuplicateNickname(userDto.getNickname());

        User user = getUser(userDto.getUserId());
        user.setNickname(userDto.getNickname());
        user.setMessage(userDto.getMessage());

        userRepository.save(user);
        return userDto.getUserId();
    }

    @Override
    public void delete(String userId) {
        User user = getUser(userId);
        userRepository.deleteAssociatedFollow(user);
        userRepository.delete(user);
    }

}
