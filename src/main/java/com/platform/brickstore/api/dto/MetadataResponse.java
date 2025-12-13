package com.platform.brickstore.api.dto;

public record MetadataResponse(
        String group,
        String artifact,
        String name,
        String version,
        String buildTime,
        String javaVersion,
        String jvmVendor,
        String osName,
        String osVersion,
        int cpuCores,
        long jvmUptimeMillis
) {}
