package com.web.curation.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FirebaseConnectionIntegrateTest {


    @Test
    void connect() throws  Exception{

        UserRecord user = FirebaseAuth.getInstance().getUserByEmail("test@test.com");

        Assertions.assertThat(user.getUid()).isEqualTo("HtoGTxWUnSXFkA9w9JbmGixNvef2");

    }

}
