package com.platform.brickstore.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.domain.entity.Currency;
import com.platform.brickstore.api.dto.CountrySimpleResponse;
import com.platform.brickstore.api.dto.CurrencyRequest;
import com.platform.brickstore.api.dto.CurrencyResponse;

/**
 * Mapper for converting between Currency entity and DTOs.
 */
@Component
public class CurrencyMapper {

    /**
     * Convert CurrencyRequest DTO to Currency entity.
     */
    public Currency toEntity(CurrencyRequest request) {
        return Currency.builder()
            .name(request.name())
            .iso3(request.iso3())
            .code(request.code())
            .symbol(request.symbol())
            .build();
    }

    /**
     * Convert Currency entity to CurrencyResponse DTO (without nested countries).
     */
    public CurrencyResponse toResponse(Currency entity) {
        return new CurrencyResponse(
            entity.getPid(),
            entity.getName(),
            entity.getIso3(),
            entity.getCode(),
            entity.getSymbol(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            null
        );
    }

    /**
     * Convert Currency entity and related Country entities to CurrencyResponse DTO with nested countries.
     * Uses a simple Country DTO to avoid recursive nesting.
     */
    public CurrencyResponse toResponse(Currency entity, List<Country> countries) {
        List<CountrySimpleResponse> countryResponses = countries == null ? null : countries.stream()
            .map(c -> new CountrySimpleResponse(
                c.getPid(),
                c.getName(),
                c.getIso2(),
                c.getIso3(),
                c.getCode(),
                c.getCreatedAt(),
                c.getUpdatedAt()
            ))
            .collect(Collectors.toList());
        
        return new CurrencyResponse(
            entity.getPid(),
            entity.getName(),
            entity.getIso3(),
            entity.getCode(),
            entity.getSymbol(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            countryResponses
        );
    }

    /**
     * Update Currency entity from CurrencyRequest DTO.
     * Note: id and pid are never updated; they are immutable.
     */
    public Currency updateEntity(CurrencyRequest request, Currency entity) {
        entity.setName(request.name());
        entity.setIso3(request.iso3());
        entity.setCode(request.code());
        entity.setSymbol(request.symbol());
        return entity;
    }
}
