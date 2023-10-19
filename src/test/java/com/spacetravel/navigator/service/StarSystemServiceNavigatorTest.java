package com.spacetravel.navigator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.spacetravel.navigator.exceptions.NoSuchRouteException;
import com.spacetravel.navigator.model.RouteFilter;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.memory.SpaceHighwayInMemoryRepository;
import com.spacetravel.navigator.repository.memory.StarSystemInMemoryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StarSystemServiceNavigatorTest {

    private final StarSystemKey sol = new StarSystemKey("solar-system");
    private final StarSystemKey alphaCentauri = new StarSystemKey("alpha-centauri");
    private final StarSystemKey sirius = new StarSystemKey("sirius");
    private final StarSystemKey betelgeuse = new StarSystemKey("betelgeuse");
    private final StarSystemKey vega = new StarSystemKey("vega");
    private final StarSystemKey nonReachable = new StarSystemKey("non-reachable");

    // Exercise 1
    @Test
    public void testCalculateTotalDurationForRoute() throws Exception {
        var service = createService();
        var route = List.of(sol, alphaCentauri, sirius);
        var duration = service.calculateTotalDurationForRoute(route);
        assertEquals(9.0, duration);
    }

    // Exercise 2
    @Test
    public void testCalculateTotalDurationForShortRoute() throws Exception {
        var service = createService();
        var route = List.of(sol, betelgeuse);
        var duration = service.calculateTotalDurationForRoute(route);
        assertEquals(5.0, duration);
    }

    // Exercise 3
    @Test
    public void testCalculateTotalDurationForMediumRoute() throws Exception {
        var service = createService();
        var route = List.of(sol, betelgeuse, sirius);
        var duration = service.calculateTotalDurationForRoute(route);
        assertEquals(13.0, duration);
    }

    // Exercise 4
    @Test
    public void testCalculateTotalDurationForLongRoute() throws Exception {
        var service = createService();
        var route = List.of(sol, vega, alphaCentauri, sirius, betelgeuse);
        var duration = service.calculateTotalDurationForRoute(route);
        assertEquals(22, duration);
    }

    // Exercise 5
    @Test
    public void testCalculateTotalDurationForInvalidRoute() throws Exception {
        var service = createService();
        var route = List.of(sol, vega,betelgeuse);
        assertThrows(NoSuchRouteException.class, () -> service.calculateTotalDurationForRoute(route));
    }

    // Exercise 6
    @Test
    public void testCalculateAllRoutesWithMaxNSteps() throws Exception {
        var service = createService();
        var routes = service.calculateAllRoutes(sirius, sirius, new RouteFilter(null, 3, null));
        assertEquals(2, routes.size());
    }

    // Exercise 7
    @Test
    public void testCalculateAllRoutesWithExactlyNSteps() throws Exception {
        var service = createService();
        var routes = service.calculateAllRoutes(sol, sirius, new RouteFilter(4, 4, null));
        assertEquals(3, routes.size());
    }

    // Exercise 8
    @Test
    public void testCalculateFastestRoute() throws Exception {
        var service = createService();
        var maybeRoute = service.calculateFastestRoute(sol, sirius);
        assertTrue(maybeRoute.isPresent());
        assertEquals(9.0, maybeRoute.get().duration());
    }

    @Test
    public void testCalculateFastestRouteNonReachable() throws Exception {
        var service = createService();
        var maybeRoute = service.calculateFastestRoute(sol, nonReachable);
        assertTrue(maybeRoute.isEmpty());
    }

    // Exercise 9
    @Test
    public void testCalculateFastestRouteSameStartAndEnd() throws Exception {
        var navigatorService = createService();
        var maybeRoute = navigatorService.calculateFastestRoute(alphaCentauri, alphaCentauri);
        assertTrue(maybeRoute.isPresent());
        assertEquals(9.0, maybeRoute.get().duration());
    }

    // Exercise 10
    @Test
    public void testCalculateAllRoutesWithDurationBelowN() throws Exception {
        var service = createService();
        var routes = service.calculateAllRoutes(sirius, sirius, new RouteFilter(null, null, 30.0));
        assertEquals(7, routes.size());
    }

    @Test
    public void testCalculateAllRoutesWithoutEndCondition() throws Exception {
        var service = createService();
        assertThrows(IllegalArgumentException.class, () -> service.calculateAllRoutes(sirius, sirius, new RouteFilter(null, null, null)));
    }

    private StarSystemService createService() throws Exception {
        var starSystemRepository = new StarSystemInMemoryRepository(false);
        // generate default star systems
        var sol = starSystemRepository.add("Solar System").key();
        var alphaCentauri = starSystemRepository.add("Alpha Centauri").key();
        var sirius = starSystemRepository.add("Sirius").key();
        var vega = starSystemRepository.add("Vega").key();
        var betelgeuse = starSystemRepository.add("Betelgeuse").key();

        // generate default space highways
        var spaceHighwayRepository = new SpaceHighwayInMemoryRepository(false);
        spaceHighwayRepository.add(sol, alphaCentauri, 5.0);
        spaceHighwayRepository.add(alphaCentauri, sirius, 4.0);
        spaceHighwayRepository.add(sirius, betelgeuse, 8.0);
        spaceHighwayRepository.add(betelgeuse, sirius, 8.0);
        spaceHighwayRepository.add(betelgeuse, vega, 6.0);
        spaceHighwayRepository.add(sol, betelgeuse, 5.0);
        spaceHighwayRepository.add(sirius, vega, 2.0);
        spaceHighwayRepository.add(vega, alphaCentauri, 3.0);
        spaceHighwayRepository.add(sol, vega, 7.0);

        return new StarSystemService(starSystemRepository, spaceHighwayRepository);
    }
}
