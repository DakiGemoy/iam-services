package com.project.iam.repositories;

import com.project.iam.entities.RequestAccessDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestAccessDetailRepo extends JpaRepository<RequestAccessDetailEntity, Long> {
}
