package com.project.iam.repositories;

import com.project.iam.entities.AccessCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessCatalogRepository extends JpaRepository<AccessCatalogEntity,Long> {
    Optional<AccessCatalogEntity> findByAccessName(String accesName);
}
