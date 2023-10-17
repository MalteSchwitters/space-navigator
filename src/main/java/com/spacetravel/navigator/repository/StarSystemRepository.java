package com.spacetravel.navigator.repository;

import com.spacetravel.navigator.model.StarSystem;
import com.spacetravel.navigator.model.StarSystemKey;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface StarSystemRepository {

    Stream<StarSystem> findAll();

    Optional<StarSystem> findByKey(@NonNull StarSystemKey key);

    StarSystem add(@NonNull String name);
}
