package com.spacetravel.navigator.service;

import com.spacetravel.navigator.model.SpaceHighway;
import com.spacetravel.navigator.model.StarSystem;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.SpaceHighwayRepository;
import com.spacetravel.navigator.repository.StarSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class StarSystemService {

    private final StarSystemRepository starSystemRepository;
    private final SpaceHighwayRepository spaceHighwayRepository;

    @Autowired
    public StarSystemService(
            StarSystemRepository starSystemRepository,
            SpaceHighwayRepository spaceHighwayRepository
    ) {
        this.starSystemRepository = starSystemRepository;
        this.spaceHighwayRepository = spaceHighwayRepository;
    }

    public Stream<StarSystem> getStarSystems() {
        return starSystemRepository.findAll();
    }

    public Optional<StarSystem> getStarSystemByKey(StarSystemKey key) {
        return starSystemRepository.findByKey(key);
    }

    public StarSystem addStarSystem(String name) {
        return starSystemRepository.add(name);
    }

    public SpaceHighway addRoute(StarSystemKey from, StarSystemKey to, double duration) {
        return spaceHighwayRepository.add(from, to, duration);
    }
}
