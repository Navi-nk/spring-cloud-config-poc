package com.navi.cloudconfig.server.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class ConfigModel implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String namespace;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(required = true)
    String profile;

    Long version;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Object> configValues;

    public ConfigModel(String namespace, String profile, Long version, Map<String, Object> configValues) {
        this.namespace = namespace;
        this.profile = profile;
        this.version = version;
        this.configValues = configValues;
    }

    public ConfigModel() {
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Map<String, Object> getConfigValues() {
        return configValues;
    }

    public void setConfigValues(Map<String, Object> configValues) {
        this.configValues = configValues;
    }
}
