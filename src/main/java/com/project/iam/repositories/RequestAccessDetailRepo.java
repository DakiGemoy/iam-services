package com.project.iam.repositories;

import com.project.iam.entities.RequestAccessDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestAccessDetailRepo extends JpaRepository<RequestAccessDetailEntity, Long> {
    List<RequestAccessDetailEntity> findByRequestAccess_Id(Long reqId);
//    void findBy
}
