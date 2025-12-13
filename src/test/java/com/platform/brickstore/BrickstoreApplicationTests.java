package com.platform.brickstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = {
	org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.class,
	org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer.class
})
class BrickstoreApplicationTests {

	@Test
	void contextLoads() {
	}

}
