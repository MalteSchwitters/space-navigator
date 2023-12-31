package com.spacetravel.navigator.adapter.rest.v1;

import com.spacetravel.errors.NotFoundException;
import com.spacetravel.navigator.adapter.rest.v1.model.RouteRepresentation;
import com.spacetravel.navigator.exceptions.NoSuchRouteException;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.service.StarSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/navigator")
public class NavigatorController {

    private final StarSystemService starSystemService;

    @Autowired
    public NavigatorController(StarSystemService starSystemService) {
        this.starSystemService = starSystemService;
    }

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/route-duration",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    public ResponseEntity<RouteRepresentation> calculateRouteDuration(@RequestBody List<String> starSystems) {
        var route = starSystems.stream().map(StarSystemKey::new).toList();
        try {
            var duration = starSystemService.calculateTotalDurationForRoute(route);
            return ResponseEntity.ok(new RouteRepresentation(starSystems, duration));
        } catch (NoSuchRouteException e) {
            throw new NotFoundException("NO SUCH ROUTE");
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/fastest-route/from/{fromStarSystem}/to/{toStarSystem}",
        produces = {"application/json"}
    )
    public ResponseEntity<RouteRepresentation> calculateFastestRoute(
        @PathVariable("fromStarSystem") String fromStarSystem,
        @PathVariable("toStarSystem") String toStarSystem
    ) {
        var from = new StarSystemKey(fromStarSystem);
        var to = new StarSystemKey(toStarSystem);

        var route = starSystemService.calculateFastestRoute(from, to)
            .map(RouteRepresentation::new)
            .orElseThrow(() -> new NotFoundException("NO SUCH ROUTE"));

        return ResponseEntity.ok(route);
    }
}
