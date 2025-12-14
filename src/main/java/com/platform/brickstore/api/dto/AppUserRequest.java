package com.platform.brickstore.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserRequest {
    private String username;
    private String password; // plain password — service may hash
}
