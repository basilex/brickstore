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
    private static final String SQL_FIND_ID_BY_PID = "SELECT id FROM rbac_role WHERE pid = ?";
    private static final String SQL_EXISTS_BY_PID = "SELECT count(1) FROM rbac_role WHERE pid = ?";
    private static final String SQL_EXISTS_BY_NAME = "SELECT count(1) FROM rbac_role WHERE name = ?";

    public Optional<RbacRole> findByPid(String pid) {
        return jdbcClient.sql(SQL_FIND_BY_PID).param(pid)
            .query(RbacRole.class).optional();
    }

    public Optional<RbacRole> findByName(String name) {
        return jdbcClient.sql(SQL_FIND_BY_NAME).param(name)
            .query(RbacRole.class)
            .optional();
    }

    public List<RbacRole> findAll() {
        return jdbcClient.sql(SQL_FIND_ALL)
            .query(RbacRole.class)
            .list();
    }

    public Optional<String> findIdByPid(String pid) {
        return jdbcClient.sql(SQL_FIND_ID_BY_PID)
        .param(pid).query(String.class)
        .optional();
    }

    public boolean existsByPid(String pid) {
        return jdbcClient.sql(SQL_EXISTS_BY_PID)
            .param(pid)
            .query(Integer.class)
            .optional()
            .map(i -> i > 0)
            .orElse(false);
    }

    public boolean existsByName(String name) {
        return jdbcClient.sql(SQL_EXISTS_BY_NAME)
            .param(name)
            .query(Integer.class)
            .optional()
            .map(i -> i > 0)
            .orElse(false);
    }

    public void save(RbacRole role) {
        jdbcClient.sql(SQL_INSERT)
            .params(role.getId()) 
            .params(role.getPid())
            .params(role.getName())
            .params(role.getDescription())
            .params(role.getCreatedAt())
            .params(role.getUpdatedAt())
            .update();
    }

    public void update(RbacRole role) {
        jdbcClient.sql(SQL_UPDATE)
            .params(role.getName())
            .params(role.getDescription())
            .params(role.getUpdatedAt())
            .params(role.getPid())
            .update();
    }

    public void deleteByPid(String pid) {
        jdbcClient.sql(SQL_DELETE).param(pid).update();
    }
}
