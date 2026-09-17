package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "request_access_history")
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccessHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_access_id", referencedColumnName = "id", nullable = false)
    private RequestAccessEntity requestAccess;

    private String oldAccessCatalog;
    private String newAccessCatalog;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assign_to_user", referencedColumnName = "id")
    private UserEntity assignedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assign_to_role", referencedColumnName = "id", nullable = false)
    private RoleEntity assignedRole;

    private Long updatedBy;
    private LocalDateTime updatedAt;
}
