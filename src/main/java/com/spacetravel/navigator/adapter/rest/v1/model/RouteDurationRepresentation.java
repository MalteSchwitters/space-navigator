package com.spacetravel.navigator.adapter.rest.v1.model;

import org.springframework.lang.NonNull;

public class RouteDurationRepresentation {
    private @NonNull Double duration;

    public RouteDurationRepresentation(double duration) {
        this.duration = duration;
    }

    @NonNull
    public Double getDuration() {
        return duration;
    }

    public void setDuration(@NonNull Double duration) {
        this.duration = duration;
    }
}
