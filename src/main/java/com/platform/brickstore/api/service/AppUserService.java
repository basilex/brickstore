package com.platform.brickstore.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.platform.brickstore.api.domain.entity.AppUser;
import com.platform.brickstore.api.exception.ErrorCode;
import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.repository.AppUserRepository;
import com.platform.brickstore.api.utility.UUIDv7;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUser getByPid(String pid) {
        return appUserRepository.findByPid(pid)
            .orElseThrow(() -> new NotFoundException("User not found with pid: " + pid, ErrorCode.USER_NOT_FOUND));
    }

    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    public List<AppUser> getAll(Integer page, Integer size) {
        if (page == null || size == null) return getAll();
        int limit = size;
        int offset = page * size;
        return appUserRepository.findAll(limit, offset);
    }

    public void create(String username, String password, Boolean enabled) {
        if (appUserRepository.existsByUsername(username)) {
            throw new DuplicateKeyException("Username already exists: " + username);
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must be provided");
        }
        var now = LocalDateTime.now();
        var hashed = passwordEncoder.encode(password);
        var user = AppUser.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .username(username)
            .password(hashed)
            .enabled(enabled == null ? true : enabled)
            .createdAt(now)
            .updatedAt(now)
            .build();
        appUserRepository.save(user);
    }

    public void updateByPid(String pid, String username, String password, Boolean enabled) {
        var user = getByPid(pid);
        user.setUsername(username);
        if (password != null) user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(enabled == null ? user.getEnabled() : enabled);
        user.setUpdatedAt(LocalDateTime.now());
        appUserRepository.update(user);
    }

    public void deleteByPid(String pid) {
        appUserRepository.deleteByPid(pid);
    }
}
