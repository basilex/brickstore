package com.platform.brickstore.api.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private String id;

    @Column("pid")
    private String pid;

    @Column("user_id")
    private String userId;

    @Column("type")
    private String type;

    @Column("value")
    private String value;

    @Column("verified")
    private Boolean verified;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
