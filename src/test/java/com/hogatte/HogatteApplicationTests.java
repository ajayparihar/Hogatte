package com.hogatte;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class HogatteApplicationTests {

	@Test
	void contextLoads() {
		HogatteApplication application = new HogatteApplication();
		assertNotNull(application);
	}

}
