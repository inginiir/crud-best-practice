package com.kalita.drone;

import com.kalita.drone.controllers.DroneController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
class DroneApplicationTests {

	private final DroneController droneController;

	@Autowired
	DroneApplicationTests(DroneController droneController) {
		this.droneController = droneController;
	}

	@Test
	void contextLoads() {
		assertNotNull(droneController);
	}
}
