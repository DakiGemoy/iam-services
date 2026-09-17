package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_access")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_catalog_id", referencedColumnName = "id", nullable = false)
    private AccessCatalogEntity catalogEntity;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
