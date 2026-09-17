package com.project.iam.repositories;

import com.project.iam.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    List<UserRoleEntity> findByRole_Id(Long roleId);
    List<UserRoleEntity> findByUser_Id(Long userId);
}
