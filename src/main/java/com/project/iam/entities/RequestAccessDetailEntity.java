package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "request_access_detail")
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccessDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_access_id", referencedColumnName = "id", nullable = false)
    private RequestAccessEntity requestAccess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_catalog_id", referencedColumnName = "id", nullable = false)
    private AccessCatalogEntity catalog;

    private String action;
}
