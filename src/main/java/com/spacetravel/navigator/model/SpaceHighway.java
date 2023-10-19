package com.spacetravel.navigator.model;

import org.springframework.lang.NonNull;

public record SpaceHighway(
    @NonNull SpaceHighwayKey key,
    @NonNull StarSystemKey from,
    @NonNull StarSystemKey to,
    @NonNull double duration
) {
}
