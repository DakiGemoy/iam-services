package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "access_catalog")
@AllArgsConstructor
@NoArgsConstructor
public class AccessCatalogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accessName;
    private String baseUrl;
    private LocalDateTime createdAt;
}
