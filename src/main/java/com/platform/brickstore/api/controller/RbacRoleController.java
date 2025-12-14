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
import org.springframework.web.bind.annotation.RestController;

import com.platform.brickstore.api.dto.RbacRoleRequest;
import com.platform.brickstore.api.dto.RbacRoleResponse;
import com.platform.brickstore.api.mapper.RbacRoleMapper;
import com.platform.brickstore.api.service.RbacRoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rbac/roles")
public class RbacRoleController {
    
    private final RbacRoleService rbacRoleService;

    @GetMapping
    public ResponseEntity<List<RbacRoleResponse>> getAll() {
        return ResponseEntity.ok(rbacRoleService.getAll().stream().map(RbacRoleMapper::toResponse).toList());
    }

    @GetMapping("/{pid}")
    public ResponseEntity<RbacRoleResponse> getByPid(@PathVariable String pid) {
        return ResponseEntity.ok(RbacRoleMapper.toResponse(rbacRoleService.getByPid(pid)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody RbacRoleRequest request) {
        rbacRoleService.create(request.name(), request.description());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{pid}")
    public ResponseEntity<Void> update(@PathVariable String pid, @Valid @RequestBody RbacRoleRequest request) {
        rbacRoleService.updateByPid(pid, request.name(), request.description());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> delete(@PathVariable String pid) {
        rbacRoleService.deleteByPid(pid);
        return ResponseEntity.ok().build();
    }
}
