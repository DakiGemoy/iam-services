package com.project.iam.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "login_failed")
public class LoginFailedEntity {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
}
