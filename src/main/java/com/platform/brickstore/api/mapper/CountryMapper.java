package com.platform.brickstore.api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.platform.brickstore.api.domain.entity.Country;
import com.platform.brickstore.api.domain.entity.Currency;
import com.platform.brickstore.api.dto.CountryRequest;
import com.platform.brickstore.api.dto.CountryResponse;

/**
 * Mapper for converting between Country entity and DTOs.
 */
@Component
public class CountryMapper {

    private final CurrencyMapper currencyMapper;

    public CountryMapper(CurrencyMapper currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    /**
     * Convert CountryRequest DTO to Country entity.
     */
    public Country toEntity(CountryRequest request) {
        return Country.builder()
            .name(request.name())
            .iso2(request.iso2())
            .iso3(request.iso3())
            .code(request.code())
            .build();
    }

    /**
     * Convert Country entity to CountryResponse DTO (without currencies).
     */
    public CountryResponse toResponse(Country entity) {
        return new CountryResponse(
            entity.getPid(),
            entity.getName(),
            entity.getIso2(),
            entity.getIso3(),
            entity.getCode(),
            null,
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    /**
     * Convert Country entity and its related Currency entities to CountryResponse DTO with nested currencies.
     */
    public CountryResponse toResponse(Country entity, List<Currency> currencies) {
        return new CountryResponse(
            entity.getPid(),
            entity.getName(),
            entity.getIso2(),
            entity.getIso3(),
            entity.getCode(),
            currencies == null ? null : currencies.stream()
                .map(currencyMapper::toResponse)
                .collect(Collectors.toList()),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    /**
     * Update Country entity from CountryRequest DTO.
     * Note: id and pid are never updated; they are immutable.
     */
    public Country updateEntity(CountryRequest request, Country entity) {
        entity.setName(request.name());
        entity.setIso2(request.iso2());
        entity.setIso3(request.iso3());
        entity.setCode(request.code());
        return entity;
    }
}
