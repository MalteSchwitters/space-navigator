package com.spacetravel.navigator.adapter.rest.v1;

import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.service.NavigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/calculate-route-duration")
    public ResponseEntity<Double> calculateRouteDuration(@RequestBody List<String> starSystems) {
        var route = starSystems.stream().map(StarSystemKey::new).toList();
        var duration = navigatorService.calculateTotalDurationForRoute(route);
        return ResponseEntity.ok(duration);
    }
}
