package com.spacetravel.navigator.repository.memory;

import com.spacetravel.navigator.model.SpaceHighway;
import com.spacetravel.navigator.model.SpaceHighwayKey;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.SpaceHighwayRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class SpaceHighwayInMemoryRepository implements SpaceHighwayRepository {

    private final List<SpaceHighway> knownRoutes = new ArrayList<>();

    public Stream<SpaceHighway> findByStartSystem(@NonNull StarSystemKey startStarSystem) {
        return knownRoutes.stream()
                .filter(it -> it.from().equals(startStarSystem));
    }

    public Optional<SpaceHighway> findByStartAndEndSystem(@NonNull StarSystemKey startStarSystem, @NonNull StarSystemKey endStarSystem) {
        return knownRoutes.stream()
                .filter(it -> it.from().equals(startStarSystem))
                .filter(it -> it.to().equals(endStarSystem))
                .findFirst();
    }

    public Optional<SpaceHighway> findByKey(@NonNull SpaceHighwayKey key) {
        return knownRoutes.stream()
                .filter(it -> it.key().equals(key))
                .findFirst();
    }

    public SpaceHighway add(@NonNull StarSystemKey startStarSystem, @NonNull StarSystemKey targetStarSystem, double duration) {
        var routeKey =  new SpaceHighwayKey(startStarSystem.value() + "_" + targetStarSystem.value());
        var maybeExistingRoute = findByKey(routeKey);
        if (maybeExistingRoute.isPresent()) {
            throw new IllegalArgumentException("route between star systems already exists");
        }
        SpaceHighway entity = new SpaceHighway(
                routeKey,
                startStarSystem,
                targetStarSystem,
                duration
        );
        knownRoutes.add(entity);
        return entity;
    }
}
