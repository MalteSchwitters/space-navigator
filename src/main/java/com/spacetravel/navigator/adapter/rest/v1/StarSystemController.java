package com.spacetravel.navigator.adapter.rest.v1;

import com.spacetravel.navigator.adapter.rest.v1.model.CreateStarSystemRequest;
import com.spacetravel.navigator.adapter.rest.v1.model.StarSystemRepresentation;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.service.StarSystemService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/star-systems")
public class StarSystemController {

    private final StarSystemService starSystemService;

    @Autowired
    public StarSystemController(StarSystemService starSystemService) {
        this.starSystemService = starSystemService;
    }

    @GetMapping()
    public ResponseEntity<List<StarSystemRepresentation>> getStarSystems() {
        var starSystems = starSystemService.getStarSystems()
                .map(StarSystemRepresentation::new)
                .toList();
        return ResponseEntity.ok(starSystems);
    }

    @PostMapping()
    public ResponseEntity<StarSystemRepresentation> addStarSystem(@RequestBody CreateStarSystemRequest request) {
        if (request.getName() == null) {
            throw new IllegalArgumentException("missing star system name");
        }

        var starSystem = starSystemService.addStarSystem(request.getName());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/by-key/" + starSystem.key().value())
                .buildAndExpand(starSystem.key().value()).toUri();

        return ResponseEntity
                .created(location)
                .body(new StarSystemRepresentation(starSystem));
    }

    @GetMapping("/by-key/{key}")
    public ResponseEntity<StarSystemRepresentation> getStarSystemByKey(@PathParam("key") String key) {
        return starSystemService
                .getStarSystemByKey(new StarSystemKey(key))
                .map(StarSystemRepresentation::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/reachable-from/{key}")
    public ResponseEntity<StarSystemRepresentation> getReachableStarSystems(@PathParam("key") String key) {
        return starSystemService
                .getStarSystemByKey(new StarSystemKey(key))
                .map(StarSystemRepresentation::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
