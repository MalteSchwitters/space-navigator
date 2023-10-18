package com.spacetravel.navigator.repository.memory;

import com.spacetravel.navigator.model.StarSystem;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.StarSystemRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class StarSystemInMemoryRepository implements StarSystemRepository {

    public Map<StarSystemKey, StarSystem> knownStarSystems = new HashMap<>();


    public StarSystemInMemoryRepository() {
        this(true);
    }

    public StarSystemInMemoryRepository(boolean createDefaultData) {
        if (createDefaultData) {
            add("Solar System");
            add("Alpha Centauri");
            add("Sirius");
            add("Betelgeuse");
            add("Vega");
        }
    }

    @Override
    public Stream<StarSystem> findAll() {
        return knownStarSystems.values().stream();
    }

    @Override
    public Optional<StarSystem> findByKey(@NonNull StarSystemKey key) {
        return Optional.ofNullable(knownStarSystems.get(key));
    }

    @Override
    public StarSystem add(@NonNull String name) {
        var key = name
                .toLowerCase()
                .replaceAll(" ", "-")
                .replaceAll("/[^a-z0-9-]/g", "");

        if (findByKey(new StarSystemKey(key)).isPresent()) {
            var collisionFreeKey = key;
            for (int i = 1; findByKey(new StarSystemKey(collisionFreeKey)).isPresent(); i++) {
                collisionFreeKey = key + "-" + i;
            }
            key = collisionFreeKey;
        }

        var starSystemKey = new StarSystemKey(key);
        var entity = new StarSystem(starSystemKey, name);
        knownStarSystems.put(starSystemKey, entity);
        return entity;
    }
}
