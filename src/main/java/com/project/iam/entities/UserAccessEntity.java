package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_access")
@AllArgsConstructor
public class UserAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long accessCatalogId;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
