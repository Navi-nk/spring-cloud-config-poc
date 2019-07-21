package com.navi.cloudconfig.server.controller;

import com.navi.cloudconfig.server.entity.ConfigStore;
import com.navi.cloudconfig.server.model.ConfigModel;
import com.navi.cloudconfig.server.repo.ConfigStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ConfigStoreController {

    @Autowired
    ConfigStoreRepository configStoreRepository;

    @PostMapping(value = "/save")
    public ConfigModel saveConfig(@RequestBody ConfigModel config) {

        String nameSpace = safeInput(config.getNamespace());
        String profile = safeInput(config.getProfile());
        validateMap(config.getConfigValues());

        Long version = getLatestVersion(nameSpace, profile);

        List<ConfigStore> configStoreItems = config.getConfigValues()
                .entrySet()
                .stream()
                .map(entry -> new ConfigStore(nameSpace, profile, version, entry.getKey(), String.valueOf(entry.getValue())))
                .collect(Collectors.toList());

        validateList(configStoreItems);

        configStoreRepository.saveAll(configStoreItems);
        config.setVersion(version);

        return config;
    }

    private Long getLatestVersion(String nameSpace, String profile) {
        Long version = configStoreRepository.findMaxVersion(nameSpace, profile);
        return version == null ? 1L : ++version;
    }

    @GetMapping("/fetch")
    public ConfigModel getConfig(@RequestParam("nameSpace") String nameSpace,
                                 @RequestParam("profile") String profile,
                                 @RequestParam("version") Long version){
        List<ConfigStore> configStoreList = configStoreRepository.findAllByNamespaceAndProfileAndVersion(nameSpace, profile, version);

        if(configStoreList.isEmpty())
        {
            throw new RuntimeException(String.format("No config found for namespace :%s, profile:%s and version:%s", nameSpace, profile, version));
        }

        Map<String, Object> items =  configStoreList
                .stream()
                .collect(Collectors.toMap(ConfigStore::getKey, ConfigStore::getValue));

        return new ConfigModel(nameSpace, profile, version, items);
    }

    private String safeInput(String input){
        if(input == null || input.isEmpty()){
            throw new RuntimeException("Input param is null/empty");
        }

        return input;
    }

    private void validateList(List input){
        if(input == null || input.isEmpty()){
            throw new RuntimeException("Input param is null/empty");
        }
    }

    private void validateMap(Map input){
        if(input == null || input.isEmpty()){
            throw new RuntimeException("Input param is null/empty");
        }
    }
}
