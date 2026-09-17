package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "request_access")
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assign_to_user", referencedColumnName = "id")
    private UserEntity assignedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assign_to_role", referencedColumnName = "id", nullable = false)
    private RoleEntity assignedRole;

    private Long updatedBy;
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private UserEntity createdBy;
}
