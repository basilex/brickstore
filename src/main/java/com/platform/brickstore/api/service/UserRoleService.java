package com.platform.brickstore.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.platform.brickstore.api.exception.NotFoundException;
import com.platform.brickstore.api.repository.AppUserRepository;
import com.platform.brickstore.api.repository.RbacRoleRepository;
import com.platform.brickstore.api.repository.RbacUserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final AppUserRepository appUserRepository;
    private final RbacRoleRepository rbacRoleRepository;
    private final RbacUserRoleRepository rbacUserRoleRepository;

    public void assignRoleByPid(String userPid, String rolePid) {
        var userIdOpt = appUserRepository.findIdByPid(userPid);
        var roleIdOpt = rbacRoleRepository.findIdByPid(rolePid);
        var userId = userIdOpt.orElseThrow(() -> new NotFoundException("User not found: " + userPid, null));
        var roleId = roleIdOpt.orElseThrow(() -> new NotFoundException("Role not found: " + rolePid, null));
        rbacUserRoleRepository.assignRole(userId, roleId, LocalDateTime.now());
    }

    public void removeRoleByPid(String userPid, String rolePid) {
        var userIdOpt = appUserRepository.findIdByPid(userPid);
        var roleIdOpt = rbacRoleRepository.findIdByPid(rolePid);
        var userId = userIdOpt.orElseThrow(() -> new NotFoundException("User not found: " + userPid, null));
        var roleId = roleIdOpt.orElseThrow(() -> new NotFoundException("Role not found: " + rolePid, null));
        rbacUserRoleRepository.removeRole(userId, roleId);
    }
}
