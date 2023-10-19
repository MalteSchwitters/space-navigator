package com.spacetravel.navigator.model;

import org.springframework.lang.Nullable;

import java.util.List;

public record RouteFilter(
        @Nullable Integer minSteps,
        @Nullable Integer maxSteps,
        @Nullable Double maxDuration
) {
}
