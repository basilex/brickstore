package com.platform.brickstore.test;

import org.hamcrest.Matcher;

import jakarta.validation.constraints.NotNull;

/**
 * Test helper utilities for Hamcrest matchers to help with generic compatibility in jsonPath assertions.
 */
public final class MatchersHelper {

    private MatchersHelper() {
    }

    @SuppressWarnings({"unchecked", "null"})
    @NotNull
    public static <T> Matcher<? super Object> asObject(@NotNull Matcher<T> m) {
        return (Matcher<? super Object>) m;
    }
}
