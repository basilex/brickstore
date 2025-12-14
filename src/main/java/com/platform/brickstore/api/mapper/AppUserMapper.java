package com.platform.brickstore.api.mapper;

import java.time.format.DateTimeFormatter;

import com.platform.brickstore.api.domain.entity.AppUser;
import com.platform.brickstore.api.dto.AppUserResponse;

public class AppUserMapper {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static AppUserResponse toResponse(AppUser u) {
        return AppUserResponse.builder()
            .pid(u.getPid())
            .username(u.getUsername())
            .enabled(u.getEnabled())
            .createdAt(u.getCreatedAt() != null ? u.getCreatedAt().format(ISO) : null)
            .updatedAt(u.getUpdatedAt() != null ? u.getUpdatedAt().format(ISO) : null)
            .build();
    }
}
