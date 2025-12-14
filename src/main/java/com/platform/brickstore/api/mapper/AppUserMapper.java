package com.platform.brickstore.api.mapper;

import com.platform.brickstore.api.domain.entity.AppUser;
import com.platform.brickstore.api.dto.AppUserResponse;

public class AppUserMapper {
    public static AppUserResponse toResponse(AppUser u) {
        return AppUserResponse.builder()
            .pid(u.getPid())
            .username(u.getUsername())
            .enabled(u.getEnabled())
            .createdAt(u.getCreatedAt() != null ? u.getCreatedAt().toString() : null)
            .updatedAt(u.getUpdatedAt() != null ? u.getUpdatedAt().toString() : null)
            .build();
    }
}
