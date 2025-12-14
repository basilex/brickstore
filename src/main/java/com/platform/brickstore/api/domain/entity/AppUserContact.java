package com.platform.brickstore.api.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_contact")
public class AppUserContact {
    @Id
    @Column("id")
    String id;

    @Column("pid")
    String pid;

    @Column("user_id")
    String userId;

    @Column("type")
    String type;

    @Column("value")
    String value;

    @Column("verified")
    Boolean verified;

    @Column("created_at")
    LocalDateTime createdAt;

    @Column("updated_at")
    LocalDateTime updatedAt;
}
