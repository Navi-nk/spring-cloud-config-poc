package com.navi.cloudconfig.server.repo;

import com.navi.cloudconfig.server.entity.ConfigStore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigStoreRepository extends CrudRepository<ConfigStore, Long> {

    @Query(value = "select max(version) from config_store where " +
            "namespace=:nameSpace " +
            "and profile=:profile", nativeQuery = true)
    Long findMaxVersion(@Param("nameSpace") String nameSpace,
                        @Param("profile") String profile);


    List<ConfigStore> findAllByNamespaceAndProfileAndVersion(@Param("nameSpace") String nameSpace,
                                                             @Param("profile") String profile,
                                                             @Param("version") Long version);

    @Query(value = "select * from config_store where " +
            "namespace = :namespace " +
            "and profile = :profile " +
            "and version = (select max(version) from config_store where " +
            "namespace = :namespace " +
            "and profile = :profile)", nativeQuery = true)
    List<ConfigStore> findAllByMaxVersion(@Param("namespace") String namespace,
                                          @Param("profile") String profile);
}
