package com.spacetravel.navigator.repository.memory;

import com.spacetravel.navigator.exceptions.RouteAlreadyExistsException;
import com.spacetravel.navigator.model.SpaceHighway;
import com.spacetravel.navigator.model.SpaceHighwayKey;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.SpaceHighwayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class SpaceHighwayInMemoryRepository implements SpaceHighwayRepository {

    private static final Logger log = LoggerFactory.getLogger(SpaceHighwayInMemoryRepository.class);
    private final Map<SpaceHighwayKey, SpaceHighway> knownRoutes = new HashMap<>();

    public SpaceHighwayInMemoryRepository() {
        this(true);
    }

    public SpaceHighwayInMemoryRepository(boolean createTestData) {
        if (createTestData) {
            final StarSystemKey sol = new StarSystemKey("solar-system");
            final StarSystemKey alphaCentauri = new StarSystemKey("alpha-centauri");
            final StarSystemKey sirius = new StarSystemKey("sirius");
            final StarSystemKey betelgeuse = new StarSystemKey("betelgeuse");
            final StarSystemKey vega = new StarSystemKey("vega");

            try {
                add(sol, alphaCentauri, 5.0);
                add(alphaCentauri, sirius, 4.0);
                add(sirius, betelgeuse, 8.0);
                add(betelgeuse, sirius, 8.0);
                add(betelgeuse, vega, 6.0);
                add(sol, betelgeuse, 5.0);
                add(sirius, vega, 2.0);
                add(vega, alphaCentauri, 3.0);
                add(sol, vega, 7.0);
            } catch (RouteAlreadyExistsException e) {
                log.error("Failed to initialize default space highways");
            }
        }
    }

    public Stream<SpaceHighway> findByStartSystem(@NonNull StarSystemKey startStarSystem) {
        return knownRoutes.values()
                .stream()
                .filter(it -> it.from().equals(startStarSystem));
    }

    public Optional<SpaceHighway> findByStartAndEndSystem(@NonNull StarSystemKey startStarSystem, @NonNull StarSystemKey endStarSystem) {
        return knownRoutes.values().stream()
                .filter(it -> it.from().equals(startStarSystem))
                .filter(it -> it.to().equals(endStarSystem))
                .findFirst();
    }

    public Optional<SpaceHighway> findByKey(@NonNull SpaceHighwayKey key) {
        return Optional.ofNullable(knownRoutes.get(key));
    }

    public SpaceHighway add(@NonNull StarSystemKey startStarSystem, @NonNull StarSystemKey targetStarSystem, double duration) throws RouteAlreadyExistsException {
        var key =  new SpaceHighwayKey(startStarSystem.value() + "_" + targetStarSystem.value());
        var maybeExistingRoute = findByKey(key);
        if (maybeExistingRoute.isPresent()) {
            log.error("Cannot add space highway between star systems " + startStarSystem + " and " + targetStarSystem + ": Route already exists");
            throw new RouteAlreadyExistsException();
        }
        SpaceHighway entity = new SpaceHighway(
                key,
                startStarSystem,
                targetStarSystem,
                duration
        );
        knownRoutes.put(key, entity);
        return entity;
    }
}
