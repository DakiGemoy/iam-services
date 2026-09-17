package com.project.iam.repositories;

import com.project.iam.entities.UserAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAccessRepository extends JpaRepository<UserAccessEntity, Long> {
    List<UserAccessEntity> findByUserEntity_Id(Long userId);
}
