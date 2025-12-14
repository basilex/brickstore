package com.platform.brickstore.api.dto;

/**
 * RBAC role response DTO as Java record.
 */
public record RbacRoleResponse(
    String pid,
    String name,
    String description,
    String createdAt,
    String updatedAt
) {}
