package com.platform.brickstore.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.platform.brickstore.api.domain.entity.RbacRole;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class RbacRoleRepository {
    private final JdbcClient jdbcClient;

    private static final String SQL_FIND_BY_PID = "SELECT id, pid, name, description, created_at, updated_at FROM rbac_role WHERE pid = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT id, pid, name, description, created_at, updated_at FROM rbac_role WHERE name = ?";
    private static final String SQL_FIND_ALL = "SELECT id, pid, name, description, created_at, updated_at FROM rbac_role ORDER BY name";
    private static final String SQL_INSERT = "INSERT INTO rbac_role (id, pid, name, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE rbac_role SET name = ?, description = ?, updated_at = ? WHERE pid = ?";
    private static final String SQL_DELETE = "DELETE FROM rbac_role WHERE pid = ?";

    public Optional<RbacRole> findByPid(String pid) {
        return jdbcClient.sql(SQL_FIND_BY_PID).param(pid).query(RbacRole.class).optional();
    }

    public Optional<RbacRole> findByName(String name) {
        return jdbcClient.sql(SQL_FIND_BY_NAME).param(name).query(RbacRole.class).optional();
    }

    public List<RbacRole> findAll() {
        return jdbcClient.sql(SQL_FIND_ALL).query(RbacRole.class).list();
    }

    public void save(RbacRole role) {
        jdbcClient.sql(SQL_INSERT)
            .params(role.getId(), role.getPid(), role.getName(), role.getDescription(), role.getCreatedAt(), role.getUpdatedAt())
            .update();
    }

    public void update(RbacRole role) {
        jdbcClient.sql(SQL_UPDATE)
            .params(role.getName(), role.getDescription(), role.getUpdatedAt(), role.getPid())
            .update();
    }

    public void deleteByPid(String pid) {
        jdbcClient.sql(SQL_DELETE).param(pid).update();
    }
}
