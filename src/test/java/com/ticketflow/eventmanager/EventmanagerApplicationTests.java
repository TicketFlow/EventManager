package com.ticketflow.eventmanager;

import com.ticketflow.eventmanager.utils.GlobalTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(GlobalTestConfiguration.class)
class EventmanagerApplicationTests {

    @Test
    void contextLoads() {
    }

}
