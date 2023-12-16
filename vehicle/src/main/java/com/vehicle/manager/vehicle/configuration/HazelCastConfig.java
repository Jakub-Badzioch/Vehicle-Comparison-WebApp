package com.vehicle.manager.vehicle.configuration;

import com.hazelcast.config.*;
import com.vehicle.manager.vehicle.dao.Vehicle;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HazelCastConfig {
    @Bean
    public Config configHazelCast(){
        final Config config = new Config()
                .setInstanceName("instance-1")
                .addMapConfig(new MapConfig()
                                .setName("vehicle")
                                .setEvictionConfig(new EvictionConfig()
                                        .setEvictionPolicy(EvictionPolicy.LFU)
                                        .setSize(10)
                                        .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                                )
                                .setTimeToLiveSeconds(500)
                );
        config.getSerializationConfig()
                .addDataSerializableFactory(1, (int id) -> (id == 1) ? new Vehicle() : null);
        return config;
    }
}

