package com.platform.brickstore.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating/updating Country (Java 21 record).
 */
public record CountryRequest(
    @NotBlank(message = "Country name cannot be blank")
    @Size(min = 1, max = 255, message = "Country name must be between 1 and 255 characters")
    String name,
    
    @NotBlank(message = "ISO2 code cannot be blank")
    @Size(min = 2, max = 2, message = "ISO2 code must be exactly 2 characters")
    @Pattern(regexp = "^[A-Z]{2}$", message = "ISO2 code must be two uppercase letters")
    String iso2,
    
    @NotBlank(message = "ISO3 code cannot be blank")
    @Size(min = 3, max = 3, message = "ISO3 code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "ISO3 code must be three uppercase letters")
    String iso3,
    
    @NotNull(message = "Country code cannot be null")
    @Min(value = 1, message = "Country code must be greater than 0")
    @Max(value = 32767, message = "Country code must be less than or equal to 32767")
    Short code
) {}
