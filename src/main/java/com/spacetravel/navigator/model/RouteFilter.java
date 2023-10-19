package com.spacetravel.navigator.model;

import org.springframework.lang.Nullable;

public record RouteFilter(
    @Nullable Integer minSteps,
    @Nullable Integer maxSteps,
    @Nullable Double maxDuration
) {
}
