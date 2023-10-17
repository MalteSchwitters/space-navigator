package com.spacetravel.navigator.adapter.rest.v1.model;

import com.spacetravel.navigator.model.StarSystem;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class StarSystemRepresentation {

    private @Nullable String key;
    private @Nullable String name;

    public StarSystemRepresentation(@NonNull StarSystem domain) {
        key = domain.key().value();
        name = domain.name();
    }

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(@Nullable String key) {
        this.key = key;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
