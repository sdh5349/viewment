package com.web.curation.service.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.web.curation.exceptions.FirebaseInvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAccountServiceImpl implements FirebaseAccountService{
    @Override
    public String getUid(String token) {
        return this.verifyToken(token).getUid();
    }

    @Override
    public FirebaseToken verifyToken(String token) {
        FirebaseToken ftoken = null;
        try{
            ftoken = FirebaseAuth.getInstance().verifyIdToken(token);
        }catch (FirebaseAuthException e){
            throw new FirebaseInvalidTokenException("허가되지 않은 토큰입니다.");
        }
        return ftoken;
    }
}
