package com.project.iam.repositories;

import com.project.iam.entities.RequestAccessEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestAccessRepository extends JpaRepository<RequestAccessEntity, Long> {
    Boolean existsByCreatedBy_IdAndStatusNotIn(Long userId, List<String> status);
    List<RequestAccessEntity> findByAssignedUser_IdOrStatusInAndCreatedBy_IdNot(Long userId, List<String> status, Long userLogin);
    List<RequestAccessEntity> findByCreatedBy_Id(Long userId, Pageable paging);
    Long countByCreatedBy_Id(Long userId);
    Optional<RequestAccessEntity> findByIdAndStatusNotInAndCreatedBy_IdNot(Long submissionId, List<String> status, Long userlogin);
}
