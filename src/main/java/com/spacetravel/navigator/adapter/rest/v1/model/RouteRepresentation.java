package com.spacetravel.navigator.adapter.rest.v1.model;

import com.spacetravel.navigator.model.Route;
import com.spacetravel.navigator.model.StarSystemKey;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;

public class RouteRepresentation {
    private @NonNull List<String> starSystems;
    private @NonNull Double duration;

    public RouteRepresentation(List<String> starSystems, double duration) {
        this.starSystems = starSystems;
        this.duration = duration;
    }

    public RouteRepresentation(Route route) {
        this.starSystems = route.starSystems().stream().map(StarSystemKey::value).toList();
        this.duration = route.duration();
    }


    @NonNull
    public Double getDuration() {
        return duration;
    }

    public void setDuration(@NonNull Double duration) {
        this.duration = duration;
    }

    @NonNull
    public List<String> getStarSystems() {
        return starSystems;
    }

    public void setStarSystems(@NonNull List<String> starSystems) {
        this.starSystems = starSystems;
    }
}
