package com.spacetravel.navigator.repository;

import com.spacetravel.navigator.exceptions.RouteAlreadyExistsException;
import com.spacetravel.navigator.model.SpaceHighway;
import com.spacetravel.navigator.model.SpaceHighwayKey;
import com.spacetravel.navigator.model.StarSystemKey;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface SpaceHighwayRepository {
    Stream<SpaceHighway> findByStartSystem(@NonNull StarSystemKey startStarSystem);

    Optional<SpaceHighway> findByStartAndEndSystem(@NonNull StarSystemKey startStarSystem, @NonNull StarSystemKey endStarSystem);

    Optional<SpaceHighway> findByKey(@NonNull SpaceHighwayKey key);

    SpaceHighway add(@NonNull StarSystemKey startStarSystem, @NonNull StarSystemKey targetStarSystem, double duration) throws RouteAlreadyExistsException;
}
