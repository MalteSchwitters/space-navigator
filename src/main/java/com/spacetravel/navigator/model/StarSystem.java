package com.spacetravel.navigator.model;

import org.springframework.lang.NonNull;

public record StarSystem(
        @NonNull StarSystemKey key,
        @NonNull String name
) {}
