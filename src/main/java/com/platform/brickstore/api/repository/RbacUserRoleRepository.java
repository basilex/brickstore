package com.platform.brickstore.api.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class RbacUserRoleRepository {
    private final JdbcClient jdbcClient;

    private static final String SQL_INSERT = "INSERT INTO rbac_user_role (id, user_id, role_id, created_at) VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM rbac_user_role WHERE user_id = ? AND role_id = ?";

    public void assignRole(String userId, String roleId, java.time.LocalDateTime createdAt) {
        jdbcClient.sql(SQL_INSERT).params(java.util.UUID.randomUUID().toString(), userId, roleId, createdAt).update();
    }

    public void removeRole(String userId, String roleId) {
        jdbcClient.sql(SQL_DELETE).params(userId, roleId).update();
    }
}
