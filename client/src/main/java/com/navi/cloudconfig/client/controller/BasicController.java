package com.navi.cloudconfig.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BasicController {

    @Autowired
    Environment environment;

    /***
     * Test Api to get all the configs read from remote config server
     */
    @GetMapping
    public Map<String, Object> propertiesFromServer() {
        Map<String, Object> result = new HashMap<>();
        ((AbstractEnvironment) environment).getPropertySources()
                .stream()
                .filter(ps -> ps instanceof CompositePropertySource)
                .map(ps -> ((CompositePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .forEach(name -> result.put(name, environment.getProperty(name)));

        return result;


    }
}
