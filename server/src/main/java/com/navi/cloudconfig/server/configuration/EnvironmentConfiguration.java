package com.navi.cloudconfig.server.configuration;

import com.navi.cloudconfig.server.repo.ConfigStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfiguration {
    @Autowired
    ConfigStoreRepository configStoreRepository;

    /**
     *Bean to provide a custom Environment Repository instance
     * instead of using one provided by spring cloud config (Git, JDBC etc)
     *
     */
    @Bean
    public CustomEnvironmentRepository customEnvironmentRepository(){
        return new CustomEnvironmentRepository(configStoreRepository);
    }

}
