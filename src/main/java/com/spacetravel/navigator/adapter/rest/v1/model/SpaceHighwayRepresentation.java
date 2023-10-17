package com.spacetravel.navigator.adapter.rest.v1.model;

import com.spacetravel.navigator.model.SpaceHighway;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class SpaceHighwayRepresentation {

    private @Nullable String key;
    private @Nullable String from;
    private @Nullable String to;
    private @Nullable Double duration;

    public SpaceHighwayRepresentation(@NonNull SpaceHighway domain) {
        key = domain.key().value();
        from = domain.from().value();
        to = domain.from().value();
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
    public String getFrom() {
        return from;
    }

    public void setFrom(@Nullable String from) {
        this.from = from;
    }

    @Nullable
    public String getTo() {
        return to;
    }

    public void setTo(@Nullable String to) {
        this.to = to;
    }

    @Nullable
    public Double getDuration() {
        return duration;
    }

    public void setDuration(@Nullable Double duration) {
        this.duration = duration;
    }
}
