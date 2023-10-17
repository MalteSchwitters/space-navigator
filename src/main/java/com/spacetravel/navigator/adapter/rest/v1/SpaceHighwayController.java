package com.spacetravel.navigator.adapter.rest.v1;

import com.spacetravel.navigator.adapter.rest.v1.model.CreateSpaceHighwayRequest;
import com.spacetravel.navigator.adapter.rest.v1.model.SpaceHighwayRepresentation;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.service.StarSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/star-routes")
public class SpaceHighwayController {

    private final StarSystemService starSystemService;

    @Autowired
    public SpaceHighwayController(StarSystemService starSystemService) {
        this.starSystemService = starSystemService;
    }

    @PostMapping()
    public ResponseEntity<SpaceHighwayRepresentation> addSpaceHighway(@RequestBody CreateSpaceHighwayRequest request) {
        if (request.getFromStarSystemKey() == null) {
            throw new IllegalArgumentException("missing from star system");
        }
        if (request.getToStarSystemKey() == null) {
            throw new IllegalArgumentException("missing to star system");
        }
        if (request.getDuration() == null) {
            throw new IllegalArgumentException("missing duration");
        }

        var from = new StarSystemKey(request.getFromStarSystemKey());
        var to = new StarSystemKey(request.getToStarSystemKey());
        var route = starSystemService.addRoute(from, to, request.getDuration());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/by-key/" + route.key().value())
                .buildAndExpand(route.key().value()).toUri();

        return ResponseEntity
                .created(location)
                .body(new SpaceHighwayRepresentation(route));
    }
}
