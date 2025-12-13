package com.platform.brickstore.api.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "rbac_role")
public class RbacRole {
    @Id
    @Column("id")
    @NotBlank(message = "RBAC role ID cannot be blank")
    @JsonIgnore
    String id;

    @Column("pid")
    @NotBlank(message = "RBAC PID cannot be blank")
    String pid;

    @Column("name")
    @NotBlank(message = "RBAC role Name cannot be blank")
    @Size(min = 1, max = 64, message = "RBAC role name must be between 1 and 64 characters")
    String name;

    @Column("description")
    @Size(max = 255, message = "RBAC role description must be less than 255 characters")
    String description;

    @Column("created_at")
    LocalDateTime createdAt;

    @Column("updated_at")
    LocalDateTime updatedAt;
}
