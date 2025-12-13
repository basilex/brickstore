package com.platform.brickstore.api.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a currency with ISO code and symbol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency")
public class Currency {

    @Id
    @Column(value = "id")
    @NotBlank(message = "Currency ID cannot be blank")
    @JsonIgnore
    String id;

    @Column(value = "pid")
    @NotBlank(message = "Currency PID cannot be blank")
    String pid;

    @Column(value = "name")
    @NotBlank(message = "Currency name cannot be blank")
    @Size(min = 1, max = 255, message = "Currency name must be between 1 and 255 characters")
    String name;

    @Column(value = "iso3")
    @NotBlank(message = "ISO3 code cannot be blank")
    @Size(min = 3, max = 3, message = "ISO3 code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "ISO3 code must be three uppercase letters")
    String iso3;

    @Column(value = "code")
    @NotNull(message = "Currency code cannot be null")
    @Min(value = 1, message = "Currency code must be greater than 0")
    @Max(value = 32767, message = "Currency code must be less than or equal to 32767")
    Short code;

    @Column(value = "symbol")
    @NotBlank(message = "Currency symbol cannot be blank")
    @Size(min = 1, max = 10, message = "Currency symbol must be between 1 and 10 characters")
    String symbol;

    @Column(value = "created_at")
    LocalDateTime createdAt;

    @Column(value = "updated_at")
    LocalDateTime updatedAt;
}
