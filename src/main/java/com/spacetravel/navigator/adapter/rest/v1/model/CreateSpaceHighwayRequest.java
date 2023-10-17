package com.spacetravel.navigator.adapter.rest.v1.model;

import org.springframework.lang.Nullable;

public class CreateSpaceHighwayRequest {

    private @Nullable String fromStarSystemKey;
    private @Nullable String toStarSystemKey;
    private @Nullable Double duration;

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
