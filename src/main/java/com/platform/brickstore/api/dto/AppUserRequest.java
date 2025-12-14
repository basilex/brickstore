package com.platform.brickstore.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserRequest {
    @NotBlank
    private String username;

    /** Plain password — required for create; optional for update callers may pass null */
    private String password;
}
