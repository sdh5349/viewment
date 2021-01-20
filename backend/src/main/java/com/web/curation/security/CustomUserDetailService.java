package com.web.curation.security;

import com.web.curation.domain.User;
import com.web.curation.domain.UserRole;
import com.web.curation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(username + "은 등록되지 않은 사용자입니다!");
        });
        final String prefix = "ROLE_";
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(user.getRole() == UserRole.USER) authorities.add(new SimpleGrantedAuthority(prefix + user.getRole()));
        else if(user.getRole() == UserRole.ADMIN) {
            authorities.add(new SimpleGrantedAuthority(prefix + UserRole.USER.getRoleName()));
            authorities.add(new SimpleGrantedAuthority(prefix + user.getRole()));
        }

        return new CustomUserDetails(user.getEmail(), "", authorities);
    }
}
