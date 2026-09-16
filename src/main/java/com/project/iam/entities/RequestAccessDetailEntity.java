package com.project.iam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "request_access_detail")
@AllArgsConstructor
public class RequestAccessDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long requestAccessId;
    private Long accessCatalogId;
    private String action;
}
