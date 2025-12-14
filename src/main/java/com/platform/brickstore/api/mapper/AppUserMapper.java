package com.platform.brickstore.api.mapper;

import java.time.format.DateTimeFormatter;

import com.platform.brickstore.api.domain.entity.AppUser;
import com.platform.brickstore.api.dto.AppUserResponse;

public class AppUserMapper {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static AppUserResponse toResponse(AppUser u) {
        return new AppUserResponse(
            u.getPid(),
            u.getUsername(),
            u.getEnabled(),
            (u.getCreatedAt() != null ? u.getCreatedAt().format(ISO) : null),
            (u.getUpdatedAt() != null ? u.getUpdatedAt().format(ISO) : null)
        );
    }
}
