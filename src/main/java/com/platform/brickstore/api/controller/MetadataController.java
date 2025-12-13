package com.platform.brickstore.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.brickstore.api.dto.MetadataResponse;
import com.platform.brickstore.api.service.MetadataService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/metadata")
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping
    public MetadataResponse getMetadata() {
        return metadataService.getMetadata();
    }
}