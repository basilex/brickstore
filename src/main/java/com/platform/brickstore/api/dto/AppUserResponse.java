package com.platform.brickstore.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserResponse {
    private String pid;
    private String username;
    private Boolean enabled;
    private String createdAt;
    private String updatedAt;
}
