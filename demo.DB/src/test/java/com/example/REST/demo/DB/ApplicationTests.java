package com.example.REST.demo.DB;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = {("uat")})
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
