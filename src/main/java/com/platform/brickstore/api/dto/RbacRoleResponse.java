package com.platform.brickstore.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RbacRoleResponse {
    private String pid;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;
}
