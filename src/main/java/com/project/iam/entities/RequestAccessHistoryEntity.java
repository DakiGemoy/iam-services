package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "request_access_history_entity")
@AllArgsConstructor
public class RequestAccessHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long requestAccessId;
    private String oldAccessCatalog;
    private String newAccessCatalog;
    private String status;
    private Long assignToUser;
    private Long assignToRole;
    private Long updatedBy;
    private LocalDateTime updatedAt;
}
