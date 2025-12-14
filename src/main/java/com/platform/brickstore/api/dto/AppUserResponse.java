package com.platform.brickstore.api.dto;

/**
 * AppUser response DTO as Java record.
 */
public record AppUserResponse(
    String pid,
    String username,
    Boolean enabled,
    String createdAt,
    String updatedAt
) {}
