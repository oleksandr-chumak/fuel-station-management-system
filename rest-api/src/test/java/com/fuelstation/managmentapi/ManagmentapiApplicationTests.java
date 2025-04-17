package com.fuelstation.managmentapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class ManagmentapiApplicationTests {

	@Test
	void contextLoads() {
	}

}
