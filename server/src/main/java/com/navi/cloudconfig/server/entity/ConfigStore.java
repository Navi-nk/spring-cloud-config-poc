package com.navi.cloudconfig.server.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "config_store")
@EntityListeners(AuditingEntityListener.class)
public class ConfigStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "namespace")
    String namespace;

    @Column(name = "profile")
    String profile;

    @Column(name = "version")
    Long version;

    @Column(name = "`key`")
    String key;

    @Column(name = "value")
    String value;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public ConfigStore(String namespace, String profile, Long version, String key, String value) {
        this.namespace = namespace;
        this.profile = profile;
        this.version = version;
        this.key = key;
        this.value = value;
    }

    public ConfigStore() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
