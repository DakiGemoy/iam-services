package com.project.iam.repositories;

import com.project.iam.entities.RequestAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestAccessRepository extends JpaRepository<RequestAccessEntity, Long> {
}
