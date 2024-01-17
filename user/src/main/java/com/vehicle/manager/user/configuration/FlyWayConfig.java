package com.vehicle.manager.user.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlyWayConfig {
    public FlyWayConfig(DataSource dataSource) {
        Flyway.configure()
                .baselineOnMigrate(true)
                .placeholderReplacement(false)
                .dataSource(dataSource)
                .load()
                .migrate();
    }
}
