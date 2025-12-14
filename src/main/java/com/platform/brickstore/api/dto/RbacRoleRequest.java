package com.platform.brickstore.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RBAC role request DTO as Java record.
 */
public record RbacRoleRequest(
    @NotBlank(message = "Role name is required")
    @Size(min = 1, max = 100, message = "Role name must be between 1 and 100 characters")
    String name,

    @Size(max = 1024, message = "Role description must be at most 1024 characters")
    String description
) {}
