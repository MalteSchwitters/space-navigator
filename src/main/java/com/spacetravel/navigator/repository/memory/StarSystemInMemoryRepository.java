package com.spacetravel.navigator.repository.memory;

import com.spacetravel.navigator.model.StarSystem;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.StarSystemRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class StarSystemInMemoryRepository implements StarSystemRepository {

    public List<StarSystem> knownStarSystems = new ArrayList<>();

    public StarSystemInMemoryRepository() {
        // generate default data
        add("Solar System");
        add("Alpha Centauri");
        add("Sirius");
        add("Betelgeuse");
        add("Vega");
    }

    @Override
    public Stream<StarSystem> findAll() {
        return knownStarSystems.stream();
    }

    @Override
    public Optional<StarSystem> findByKey(@NonNull StarSystemKey key) {
        return knownStarSystems.stream()
                .filter(it -> it.key().equals(key))
                .findFirst();
    }

    @Override
    public StarSystem add(@NonNull String name) {
        var key = name
                .toLowerCase()
                .replaceAll(" ", "-")
                .replaceAll("/[^a-z0-9-]/g", "");

        var entity = new StarSystem(new StarSystemKey(key), name);
        knownStarSystems.add(entity);
        return entity;
    }
}
