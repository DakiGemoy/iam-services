package com.project.iam.repositories;

import com.project.iam.entities.LoginFailedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginFailedRepository extends JpaRepository<LoginFailedEntity, Long> {
}
