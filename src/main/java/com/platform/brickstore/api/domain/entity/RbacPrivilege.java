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
@Table(name = "rbac_privilege")
public class RbacPrivilege {
    @Id
    @Column("id")
    @NotBlank(message = "RBAC privilege ID cannot be blank")
    @JsonIgnore
    String id;

    @Column("pid")
    @NotBlank(message = "RBAC privilege PID cannot be blank")
    String pid;

    @Column("name")
    @NotBlank(message = "RBAC privilege Name cannot be blank")
    @Size(min = 1, max = 64, message = "RBAC privilege name must be between 1 and 64 characters")
    String name;

    @Column("description")
    @Size(max = 255, message = "RBAC privilege Description must be less than 255 characters")
    String description;

    @Column("created_at")
    LocalDateTime createdAt;

    @Column("updated_at")
    LocalDateTime updatedAt;
}
