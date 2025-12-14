package com.platform.brickstore.api.repository;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestFlywayConfig {
    @Bean
    @Primary
    public Flyway testFlyway(DataSource dataSource) {
        return Flyway.configure()
            .dataSource(dataSource)
            // Use a test-only migration location that contains a minimal schema without seed data
            .locations("classpath:db/test-migration")
            .baselineOnMigrate(true)
            // Disable validation on startup for tests: tests call clean() and migrate() explicitly
            .validateOnMigrate(false)
            .cleanDisabled(false)
            .load();
    }
}
