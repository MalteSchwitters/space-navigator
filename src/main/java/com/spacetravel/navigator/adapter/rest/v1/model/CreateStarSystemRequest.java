package com.spacetravel.navigator.adapter.rest.v1.model;

import org.springframework.lang.Nullable;

public class CreateStarSystemRequest {

    private @Nullable String name;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
