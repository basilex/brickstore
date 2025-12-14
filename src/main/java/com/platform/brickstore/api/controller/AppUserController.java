package com.platform.brickstore.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.brickstore.api.dto.AppUserRequest;
import com.platform.brickstore.api.dto.AppUserResponse;
import com.platform.brickstore.api.mapper.AppUserMapper;
import com.platform.brickstore.api.service.AppUserService;
import com.platform.brickstore.api.service.UserRoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserService appUserService;
    private final UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ResponseEntity.ok(appUserService.getAll(page, size).stream().map(AppUserMapper::toResponse).toList());
    }

    @GetMapping("/{pid}")
    public ResponseEntity<AppUserResponse> getByPid(@PathVariable String pid) {
        return ResponseEntity.ok(AppUserMapper.toResponse(appUserService.getByPid(pid)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody AppUserRequest request) {
        appUserService.create(request.username(), request.password(), true);
        // Return 201 Created with Location header pointing to the created resource by pid
        // Note: service currently generates the pid internally; we could return it if service
        // exposed the created pid. For now return 201 without body.
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{pid}")
    public ResponseEntity<Void> update(@PathVariable String pid, @Valid @RequestBody AppUserRequest request) {
        appUserService.updateByPid(pid, request.username(), request.password(), true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> delete(@PathVariable String pid) {
        appUserService.deleteByPid(pid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userPid}/roles/{rolePid}")
    public ResponseEntity<Void> assignRole(@PathVariable String userPid, @PathVariable String rolePid) {
        userRoleService.assignRoleByPid(userPid, rolePid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userPid}/roles/{rolePid}")
    public ResponseEntity<Void> removeRole(@PathVariable String userPid, @PathVariable String rolePid) {
        userRoleService.removeRoleByPid(userPid, rolePid);
        return ResponseEntity.ok().build();
    }
}
