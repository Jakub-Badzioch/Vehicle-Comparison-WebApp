package com.vehicle.manager.user.configuration;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@DependsOn("entityManagerFactory")
@RequiredArgsConstructor
@Configuration
public class FlyWayConfig implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) {
        Flyway.configure()
                .baselineOnMigrate(true)
                .placeholderReplacement(false)
                .dataSource(dataSource)
                .load()
                .migrate();
    }
}
