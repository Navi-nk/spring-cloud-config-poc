package com.navi.cloudconfig.server.configuration;

import com.navi.cloudconfig.server.entity.ConfigStore;
import com.navi.cloudconfig.server.repo.ConfigStoreRepository;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomEnvironmentRepository implements EnvironmentRepository, Ordered {

    private ConfigStoreRepository configStoreRepository;

    public CustomEnvironmentRepository(ConfigStoreRepository configStoreRepository) {
        this.configStoreRepository = configStoreRepository;
    }

    /**
     * Return Environment object for provided application and profile,
     * By design, the client integrating with this config server will have to send "latest"
     * in label argument which would in turn get max version config.
     * A Long value can also be sent but it is not type safe
     */
    @Override
    public Environment findOne(String application, String profile, String label) {
        Environment environment = new Environment(application, new String[]{profile}, label, null, null);
        List<ConfigStore> configStoreList;
        if (label == null || label.equals("latest")) {
            configStoreList = configStoreRepository.findAllByMaxVersion(application, profile);
        } else {
            configStoreList = configStoreRepository.findAllByNamespaceAndProfileAndVersion(application, profile, Long.valueOf(label));
        }

        Map<String, Object> configMap = configStoreList
                .stream()
                .collect(Collectors.toMap(ConfigStore::getKey, ConfigStore::getValue));

        environment.add(new PropertySource( application + "-" + profile, configMap));
        return environment;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
