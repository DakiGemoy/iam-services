package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "request_access")
@AllArgsConstructor
public class RequestAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private Long assignToUser;
    private Long assignToRole;
    private Long updatedBy;
    private LocalDateTime lastUpdated;
}
