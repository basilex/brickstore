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
    @NotBlank
    @JsonIgnore
    String id;

    @Column("pid")
    @NotBlank
    String pid;

    @Column("name")
    @NotBlank
    @Size(max = 64)
    String name;

    @Column("description")
    @Size(max = 255)
    String description;

    @Column("created_at")
    LocalDateTime createdAt;

    @Column("updated_at")
    LocalDateTime updatedAt;

    // ...existing code...
}
