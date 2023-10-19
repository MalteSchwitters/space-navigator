package com.spacetravel.navigator.model;

import org.springframework.lang.NonNull;
import java.util.List;

public record Route(
        @NonNull List<StarSystemKey> starSystems,
        @NonNull double duration
) {
}
