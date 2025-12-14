package com.platform.brickstore.test;

import org.hamcrest.Matcher;

/**
 * Test helper utilities for Hamcrest matchers to help with generic compatibility in jsonPath assertions.
 */
public final class MatchersHelper {

    private MatchersHelper() {
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<? super Object> asObject(Matcher<T> m) {
        return (Matcher<? super Object>) m;
    }
}
