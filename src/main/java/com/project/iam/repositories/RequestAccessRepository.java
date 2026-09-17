package com.project.iam.repositories;

import com.project.iam.entities.RequestAccessEntity;
import com.project.iam.models.response.DashboardQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RequestAccessRepository extends JpaRepository<RequestAccessEntity, Long> {
    Boolean existsByCreatedBy_IdAndStatusNotIn(Long userId, List<String> status);
    List<RequestAccessEntity> findByAssignedUser_IdOrStatusInAndCreatedBy_IdNot(Long userId, List<String> status, Long userLogin);
    List<RequestAccessEntity> findByCreatedBy_Id(Long userId, Pageable paging);
    Long countByCreatedBy_Id(Long userId);
    Optional<RequestAccessEntity> findByIdAndStatusNotInAndCreatedBy_IdNot(Long submissionId, List<String> status, Long userlogin);
    @Query(value = """
            SELECT status, COUNT(*) total
            FROM request_access
            GROUP BY status
            """, nativeQuery = true)
    List<DashboardQuery> getDashboardData();
}
