package com.project.iam.repositories;

import com.project.iam.entities.AccessCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessCatalogRepository extends JpaRepository<AccessCatalogEntity,Long> {
}
