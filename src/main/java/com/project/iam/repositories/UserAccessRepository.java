package com.project.iam.repositories;

import com.project.iam.entities.UserAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccessRepository extends JpaRepository<UserAccessEntity, Long> {
    List<UserAccessEntity> findByUserEntity_Username(String username);
    List<UserAccessEntity> findByUserEntity_Id(Long userId);
    Boolean existsByUserEntity_IdAndCatalogEntity_Id(Long userId, Long catalogId);
    Boolean existsByUserEntity_IdAndCatalogEntity_IdNot(Long userId, Long catalogId);
}
