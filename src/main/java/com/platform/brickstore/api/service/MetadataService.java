package com.platform.brickstore.api.service;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import com.platform.brickstore.api.dto.MetadataResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MetadataService {

    private final BuildProperties buildProperties;

    public MetadataResponse getMetadata() {

        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

        return new MetadataResponse(
                // ---- BUILD INFO ----
            buildProperties.getGroup(),
                buildProperties.getArtifact(),
                buildProperties.getName(),
                buildProperties.getVersion(),
                buildProperties.getTime().toString(),
                // ---- SYSTEM INFO ----
                System.getProperty("java.version"),
                System.getProperty("java.vendor"),
                System.getProperty("os.name"),
                System.getProperty("os.version"),

                Runtime.getRuntime().availableProcessors(),
                runtime.getUptime()
        );
    }
}