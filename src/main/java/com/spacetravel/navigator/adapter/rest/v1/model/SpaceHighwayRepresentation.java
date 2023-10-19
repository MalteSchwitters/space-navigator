package com.spacetravel.navigator.adapter.rest.v1.model;

import com.spacetravel.navigator.model.SpaceHighway;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class SpaceHighwayRepresentation {

    private @Nullable String key;
    private @Nullable String fromStarSystemKey;
    private @Nullable String toStarSystemKey;
    private @Nullable Double duration;

    public SpaceHighwayRepresentation(@NonNull SpaceHighway domain) {
        key = domain.key().value();
        fromStarSystemKey = domain.from().value();
        toStarSystemKey = domain.to().value();
        duration = domain.duration();
    }

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(@Nullable String key) {
        this.key = key;
    }

    @Nullable
    public String getFromStarSystemKey() {
        return fromStarSystemKey;
    }

    public void setFromStarSystemKey(@Nullable String fromStarSystemKey) {
        this.fromStarSystemKey = fromStarSystemKey;
    }

    @Nullable
    public String getToStarSystemKey() {
        return toStarSystemKey;
    }

    public void setToStarSystemKey(@Nullable String toStarSystemKey) {
        this.toStarSystemKey = toStarSystemKey;
    }

    @Nullable
    public Double getDuration() {
        return duration;
    }

    public void setDuration(@Nullable Double duration) {
        this.duration = duration;
    }
}
