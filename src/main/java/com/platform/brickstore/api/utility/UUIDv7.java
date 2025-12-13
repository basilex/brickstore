package com.platform.brickstore.api.utility;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

public final class UUIDv7 {

    private static final SecureRandom random = new SecureRandom();

    public static UUID generate() {
        // Unix timestamp in milliseconds (48 bits)
        long timestampMs = Instant.now().toEpochMilli();

        // --- High 64 bits ---
        long ms48 = (timestampMs & 0xFFFFFFFFFFFFL) << 16;      // shift to upper bits
        long version = 0x7L << 12;                              // UUIDv7 → version = 0111
        long high = ms48 | version | (random.nextInt(1 << 12)); // add 12 random bits

        // --- Low 64 bits ---
        long randLow = random.nextLong();

        // Set variant "10xx"
        randLow = (randLow & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L;

        return new UUID(high, randLow);
    }
}
