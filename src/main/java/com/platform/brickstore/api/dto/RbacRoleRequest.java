package com.platform.brickstore.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RbacRoleRequest {
    private String name;
    private String description;
}
