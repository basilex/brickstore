package com.platform.brickstore.api.mapper;

import java.time.format.DateTimeFormatter;

import com.platform.brickstore.api.domain.entity.RbacRole;
import com.platform.brickstore.api.dto.RbacRoleResponse;

public class RbacRoleMapper {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static RbacRoleResponse toResponse(RbacRole role) {
        return new RbacRoleResponse(
            role.getPid(),
            role.getName(),
            role.getDescription(),
            (role.getCreatedAt() != null ? role.getCreatedAt().format(ISO) : null),
            (role.getUpdatedAt() != null ? role.getUpdatedAt().format(ISO) : null)
        );
    }
}
