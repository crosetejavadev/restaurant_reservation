package com.crisper.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.crisper.reservation.business.service.ReservationService;

@AutoConfigureMockMvc
@SpringBootTest
class ReservationApplicationTests {
    
	@Test
	void contextLoads() {
		
	}
}
