package com.web.curation.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConnectionTest {

    FirebaseOptions options;

    @BeforeEach
    void setUp() throws Exception{
        FileInputStream serviceAccount =
                new FileInputStream(
                        "D:\\Project\\sub-pjt1\\skeleton-project_real\\backend\\src\\main\\resources\\firebase\\ssafy-user-test-firebase-adminsdk-9da3f-14c2e75ce1.json");
        options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
    }

    @Test
    void connectionTest() throws Exception {
        UserRecord user = FirebaseAuth.getInstance().getUserByEmail("test@test.com");
        String uid = user.getUid();
        Assertions.assertThat(uid).isEqualTo("HtoGTxWUnSXFkA9w9JbmGixNvef2");
    }

    @Test
    void get_not_exist_user() throws Exception {
        org.junit.jupiter.api.Assertions.assertThrows(FirebaseAuthException.class, ()->{
            FirebaseAuth.getInstance().getUserByEmail("test1@test.com");
        });
    }
}
