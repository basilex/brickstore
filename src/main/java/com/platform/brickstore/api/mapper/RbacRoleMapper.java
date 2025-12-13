package com.platform.brickstore.api.mapper;

import com.platform.brickstore.api.domain.entity.RbacRole;
import com.platform.brickstore.api.dto.RbacRoleResponse;

public class RbacRoleMapper {
    public static RbacRoleResponse toResponse(RbacRole role) {
        return RbacRoleResponse.builder()
                .pid(role.getPid())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt() != null ? role.getCreatedAt().toString() : null)
                .updatedAt(role.getUpdatedAt() != null ? role.getUpdatedAt().toString() : null)
                .build();
    }
}
