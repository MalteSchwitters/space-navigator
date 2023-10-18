package com.spacetravel.navigator.adapter.rest.v1;

import com.spacetravel.errors.NotFoundException;
import com.spacetravel.navigator.exceptions.NoSuchRouteException;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.service.StarSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
        method = RequestMethod.GET,
        value = "/calculate/route-duration",
        produces = { "text/plain" },
        consumes = { "application/json" }
    )
    public ResponseEntity<Double> calculateRouteDuration(@RequestBody List<String> starSystems) {
        var route = starSystems.stream().map(StarSystemKey::new).toList();
        try {
            var duration = starSystemService.calculateTotalDurationForRoute(route);
            return ResponseEntity.ok(duration);
        } catch (NoSuchRouteException e) {
            throw new NotFoundException("NO SUCH ROUTE");
        }
    }

    @RequestMapping(
        method = RequestMethod.GET,
        value = "/calculate/fastest-route/from/{fromStarSystem}/to/{toStarSystem}",
        produces = { "application/json" }
    )
    public ResponseEntity<List<String>> calculateRouteDuration(
            @PathVariable("fromStarSystem") String fromStarSystem,
            @PathVariable("toStarSystem") String toStarSystem
    ) {
        var from = new StarSystemKey(fromStarSystem);
        var to = new StarSystemKey(toStarSystem);
        try {
            var route = starSystemService.calculateFastestRoute(from, to);
            return ResponseEntity.ok(route.stream().map(StarSystemKey::value).toList());
        } catch (NoSuchRouteException e) {
            throw new NotFoundException("NO SUCH ROUTE");
        }
    }
}
