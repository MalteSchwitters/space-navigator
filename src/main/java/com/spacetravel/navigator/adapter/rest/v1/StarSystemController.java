package com.spacetravel.navigator.adapter.rest.v1;

import com.spacetravel.errors.BadRequestException;
import com.spacetravel.errors.NotFoundException;
import com.spacetravel.navigator.adapter.rest.v1.model.CreateStarSystemRequest;
import com.spacetravel.navigator.adapter.rest.v1.model.StarSystemRepresentation;
import com.spacetravel.navigator.exceptions.InvalidStarSystemException;
import com.spacetravel.navigator.model.StarSystem;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.service.StarSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(
        method = RequestMethod.GET,
        value = "",
        produces = { "application/json" }
    )
    public ResponseEntity<List<StarSystemRepresentation>> getStarSystems() {
        var starSystems = starSystemService.getStarSystems()
                .map(StarSystemRepresentation::new)
                .toList();
        return ResponseEntity.ok(starSystems);
    }

    @RequestMapping(
        method = RequestMethod.POST,
        value = "",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    public ResponseEntity<StarSystemRepresentation> addStarSystem(@RequestBody CreateStarSystemRequest request) {
        if (request.getName() == null) {
            throw new BadRequestException("BAD REQUEST: missing star system name");
        }

        try {
            StarSystem starSystem = starSystemService.addStarSystem(request.getName());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/by-key/" + starSystem.key().value())
                    .buildAndExpand(starSystem.key().value()).toUri();
            return ResponseEntity
                    .created(location)
                    .body(new StarSystemRepresentation(starSystem));
        } catch (InvalidStarSystemException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping("/by-key/{key}")
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/by-key/{key}",
        produces = { "application/json" }
    )
    public ResponseEntity<StarSystemRepresentation> getStarSystemByKey(@PathVariable("key") String key) {
        return starSystemService
                .getStarSystemByKey(new StarSystemKey(key))
                .map(StarSystemRepresentation::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("STAR SYSTEM NOT FOUND"));
    }
}
