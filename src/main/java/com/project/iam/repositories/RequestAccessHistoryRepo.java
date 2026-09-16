package com.project.iam.repositories;

import com.project.iam.entities.RequestAccessHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestAccessHistoryRepo extends JpaRepository<RequestAccessHistoryEntity, Long> {
}
