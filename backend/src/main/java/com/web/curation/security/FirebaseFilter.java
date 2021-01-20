package com.web.curation.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.web.curation.security.auth.FirebaseAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@Component
public class FirebaseFilter extends OncePerRequestFilter {

    //private final FirebaseAccountService accountService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader("X-Authorization-Firebase");
        if(token == null) {
            filterChain.doFilter(request, response);
            return;
        }
    
        //ToDo 토큰저장소를 캐시
        //전달받은 토큰이 유효한 토큰인지 Firebase에서 인증
        FirebaseToken decodedToken = null;
        try{
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        }catch (FirebaseAuthException e){
            decodedToken = null;
        }

        if(decodedToken != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(decodedToken.getUid());
            FirebaseAuthenticationToken fba_token = new FirebaseAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            fba_token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(fba_token);
        }
        filterChain.doFilter(request, response);
    }
}
