package com.web.curation.service.pin;

import com.web.curation.exceptions.ElementNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PinServiceTest {

    @Autowired
    PinService pinService;

    @Test
    void PinNotFound_Error() throws Exception{
        //then
        Assertions.assertThrows(ElementNotFoundException.class, ()->pinService.getPin(0L));
    }

}
