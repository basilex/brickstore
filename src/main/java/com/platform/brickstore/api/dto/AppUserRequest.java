package com.platform.brickstore.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating/updating AppUser as a Java record.
 *
 * Notes:
 * - `username` must be present for create/update and follow a conservative character set.
 * - `password` is optional for updates (may be null) but when present must meet length rules.
 */
public record AppUserRequest(
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username may contain letters, digits, dot, underscore and hyphen only")
    String username,

    /** Plain password — required for create; optional for update callers may pass null */
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters when provided")
    String password
) {}
