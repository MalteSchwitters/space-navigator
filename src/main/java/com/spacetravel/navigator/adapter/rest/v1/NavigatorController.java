package com.spacetravel.navigator.adapter.rest.v1;

import com.spacetravel.errors.NotFoundException;
import com.spacetravel.navigator.exceptions.NoSuchRouteException;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.service.NavigatorService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/navigator")
public class NavigatorController {

    private final NavigatorService navigatorService;

    @Autowired
    public NavigatorController(NavigatorService navigatorService) {
        this.navigatorService = navigatorService;
    }

    @PostMapping("/calculate/route-duration")
    public ResponseEntity<Double> calculateRouteDuration(@RequestBody List<String> starSystems) {
        var route = starSystems.stream().map(StarSystemKey::new).toList();
        try {
            var duration = navigatorService.calculateTotalDurationForRoute(route);
            return ResponseEntity.ok(duration);
        } catch (NoSuchRouteException e) {
            throw new NotFoundException("NO SUCH ROUTE");
        }
    }

    @GetMapping("/calculate/fastest-route/from{fromStarSystem}/to/{toStarSystem}")
    public ResponseEntity<List<String>> calculateRouteDuration(
            @PathParam("fromStarSystem") String fromStarSystem,
            @PathParam("toStarSystem") String toStarSystem
    ) {
        var from = new StarSystemKey(fromStarSystem);
        var to = new StarSystemKey(toStarSystem);
        try {
            var route = navigatorService.calculateFastestRoute(from, to);
            return ResponseEntity.ok(route.stream().map(StarSystemKey::value).toList());
        } catch (NoSuchRouteException e) {
            throw new NotFoundException("NO SUCH ROUTE");
        }
    }
}
