package com.spacetravel.navigator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.spacetravel.navigator.exceptions.NoSuchRouteException;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.StarSystemRepository;
import com.spacetravel.navigator.repository.memory.SpaceHighwayInMemoryRepository;
import com.spacetravel.navigator.service.NavigatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class NavigatorServiceTest {


    private final StarSystemKey sol = new StarSystemKey("solar-system");
    private final StarSystemKey alphaCentauri = new StarSystemKey("alpha-centauri");
    private final StarSystemKey sirius = new StarSystemKey("sirius");
    private final StarSystemKey betelgeuse = new StarSystemKey("betelgeuse");
    private final StarSystemKey vega = new StarSystemKey("vega");
    private final StarSystemKey nonReachable = new StarSystemKey("non-reachable");

    // Exercise 1
    @Test
    public void testCalculateTotalDurationForRoute() {
        var navigatorService = generateTestNavigatorService();
        var route = List.of(sol, alphaCentauri, sirius);
        var duration = navigatorService.calculateTotalDurationForRoute(route);
        assertEquals(9.0, duration);
    }

    // Exercise 2
    @Test
    public void testCalculateTotalDurationForShortRoute() {
        var navigatorService = generateTestNavigatorService();
        var route = List.of(sol, betelgeuse);
        var duration = navigatorService.calculateTotalDurationForRoute(route);
        assertEquals(5.0, duration);
    }

    // Exercise 3
    @Test
    public void testCalculateTotalDurationForMediumRoute() {
        var navigatorService = generateTestNavigatorService();
        var route = List.of(sol, betelgeuse, sirius);
        var duration = navigatorService.calculateTotalDurationForRoute(route);
        assertEquals(13.0, duration);
    }

    // Exercise 4
    @Test
    public void testCalculateTotalDurationForLongRoute() {
        var navigatorService = generateTestNavigatorService();
        var route = List.of(sol, vega, alphaCentauri, sirius, betelgeuse);
        var duration = navigatorService.calculateTotalDurationForRoute(route);
        assertEquals(22, duration);
    }

    // Exercise 5
    @Test
    public void testCalculateTotalDurationForInvalidRoute() {
        var navigatorService = generateTestNavigatorService();
        var route = List.of(sol, vega,betelgeuse);
        assertThrows(NoSuchRouteException.class, () -> navigatorService.calculateTotalDurationForRoute(route));
    }

    // Exercise 8
    @Test
    public void testCalculateFastestRoute() {
        var navigatorService = generateTestNavigatorService();
        var route = navigatorService.calculateFastestRoute(sol, sirius);
        var duration = navigatorService.calculateTotalDurationForRoute(route);
        assertEquals(9.0, duration);
    }

    @Test
    public void testCalculateFastestRouteNonReachable() {
        var navigatorService = generateTestNavigatorService();
        assertThrows(NoSuchRouteException.class, () -> navigatorService.calculateFastestRoute(sol, nonReachable));
    }

    // Exercise 9
    @Test
    public void testCalculateFastestRouteSameStartAndEnd() {
        var navigatorService = generateTestNavigatorService();
        var route = navigatorService.calculateFastestRoute(alphaCentauri, alphaCentauri);
        var duration = navigatorService.calculateTotalDurationForRoute(route);
        assertEquals(9.0, duration);
    }

    private NavigatorService generateTestNavigatorService() {
        var spaceHighwayRepository = new SpaceHighwayInMemoryRepository();

        // generate default space highways
        spaceHighwayRepository.add(sol, alphaCentauri, 5.0);
        spaceHighwayRepository.add(alphaCentauri, sirius, 4.0);
        spaceHighwayRepository.add(sirius, betelgeuse, 8.0);
        spaceHighwayRepository.add(betelgeuse, sirius, 8.0);
        spaceHighwayRepository.add(betelgeuse, vega, 6.0);
        spaceHighwayRepository.add(sol, betelgeuse, 5.0);
        spaceHighwayRepository.add(sirius, vega, 2.0);
        spaceHighwayRepository.add(vega, alphaCentauri, 3.0);
        spaceHighwayRepository.add(sol, vega, 7.0);

        return new NavigatorService(spaceHighwayRepository);
    }
}
