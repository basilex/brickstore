package com.platform.brickstore.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.platform.brickstore.api.domain.entity.AppUser;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class AppUserRepository {
    private final JdbcClient jdbcClient;

    private static final String SQL_FIND_BY_PID = "SELECT id, pid, username, password, enabled, created_at, updated_at FROM app_user WHERE pid = ?";
    private static final String SQL_FIND_ID_BY_PID = "SELECT id FROM app_user WHERE pid = ?";
    private static final String SQL_FIND_ALL = "SELECT id, pid, username, password, enabled, created_at, updated_at FROM app_user ORDER BY username";
    private static final String SQL_FIND_PAGED = "SELECT id, pid, username, password, enabled, created_at, updated_at FROM app_user ORDER BY username LIMIT ? OFFSET ?";
    private static final String SQL_INSERT = "INSERT INTO app_user (id, pid, username, password, enabled, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE app_user SET username = ?, password = ?, enabled = ?, updated_at = ? WHERE pid = ?";
    private static final String SQL_DELETE = "DELETE FROM app_user WHERE pid = ?";
    private static final String SQL_EXISTS_BY_USERNAME = "SELECT count(1) FROM app_user WHERE username = ?";

    public Optional<AppUser> findByPid(String pid) {
        return jdbcClient.sql(SQL_FIND_BY_PID).param(pid).query(AppUser.class).optional();
    }

    public Optional<String> findIdByPid(String pid) {
        return jdbcClient.sql(SQL_FIND_ID_BY_PID).param(pid).query(String.class).optional();
    }

    public List<AppUser> findAll() {
        return jdbcClient.sql(SQL_FIND_ALL).query(AppUser.class).list();
    }

    public List<AppUser> findAll(int limit, int offset) {
        return jdbcClient.sql(SQL_FIND_PAGED).params(limit, offset).query(AppUser.class).list();
    }

    public boolean existsByUsername(String username) {
        return jdbcClient.sql(SQL_EXISTS_BY_USERNAME).param(username).query(Integer.class).optional().map(i -> i > 0).orElse(false);
    }

    public void save(AppUser u) {
        jdbcClient.sql(SQL_INSERT)
            .params(u.getId(), u.getPid(), u.getUsername(), u.getPassword(), u.getEnabled(), u.getCreatedAt(), u.getUpdatedAt())
            .update();
    }

    public void update(AppUser u) {
        jdbcClient.sql(SQL_UPDATE)
            .params(u.getUsername(), u.getPassword(), u.getEnabled(), u.getUpdatedAt(), u.getPid())
            .update();
    }

    public void deleteByPid(String pid) {
        jdbcClient.sql(SQL_DELETE).param(pid).update();
    }
}
