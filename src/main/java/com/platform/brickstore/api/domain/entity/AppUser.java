package com.platform.brickstore.api.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class AppUser {
    @Id
    @Column("id")
    @NotBlank
    String id;

    @Column("pid")
    @NotBlank
    String pid;

    @Column("username")
    @NotBlank
    @Size(max = 64)
    String username;

    @Column("password")
    @NotBlank
    @Size(max = 255)
    String password;

    @Column("enabled")
    Boolean enabled;

    @Column("created_at")
    LocalDateTime createdAt;

    @Column("updated_at")
    LocalDateTime updatedAt;
}
