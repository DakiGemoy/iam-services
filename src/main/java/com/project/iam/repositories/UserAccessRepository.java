package com.project.iam.repositories;

import com.project.iam.entities.UserAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessRepository extends JpaRepository<UserAccessEntity, Long> {
}
