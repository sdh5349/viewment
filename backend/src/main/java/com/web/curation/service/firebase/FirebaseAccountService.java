package com.web.curation.service.firebase;

import com.google.firebase.auth.FirebaseToken;

public interface FirebaseAccountService {
    public String getUid(String token);
    public FirebaseToken verifyToken(String token);
}
