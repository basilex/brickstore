package com.platform.brickstore.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.platform.brickstore.api.domain.entity.RbacRole;
import com.platform.brickstore.api.exception.ErrorCode;
import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.repository.RbacRoleRepository;
import com.platform.brickstore.api.utility.UUIDv7;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RbacRoleService {
    private final RbacRoleRepository rbacRoleRepository;

    public RbacRole getByPid(String pid) {
        return rbacRoleRepository.findByPid(pid)
            .orElseThrow(() -> new NotFoundException("Role not found with pid: " + pid, ErrorCode.ROLE_NOT_FOUND));
    }

    public RbacRole getByName(String name) {
        return rbacRoleRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException("Role not found with name: " + name, ErrorCode.ROLE_NOT_FOUND));
    }

    public List<RbacRole> getAll() {
        return rbacRoleRepository.findAll();
    }

    public void create(String name, String description) {
        var now = LocalDateTime.now();
        var role = RbacRole.builder()
            .id(UUIDv7.generate().toString())
            .pid(UUIDv7.generate().toString())
            .name(name)
            .description(description)
            .createdAt(now)
            .updatedAt(now)
            .build();
        rbacRoleRepository.save(role);
    }

    public void updateByPid(String pid, String name, String description) {
        var role = getByPid(pid);
        role.setName(name);
        role.setDescription(description);
        role.setUpdatedAt(LocalDateTime.now());
        rbacRoleRepository.update(role);
    }

    public void deleteByPid(String pid) {
        rbacRoleRepository.deleteByPid(pid);
    }
}
