package com.spacetravel.navigator.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.spacetravel.navigator.exceptions.InvalidRouteException;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.memory.SpaceHighwayInMemoryRepository;
import com.spacetravel.navigator.repository.memory.StarSystemInMemoryRepository;
import org.junit.jupiter.api.Test;

public class StarSystemServiceCRUDTest {

    @Test
    public void testAddStarSystem() throws Exception {
        var starSystemService = generateTestStarSystemService();
        // assert start star system count
        var beforeStarSystemCount = starSystemService.getStarSystems().count();
        assertEquals(5, beforeStarSystemCount);
        // test create
        var starSystem1 = starSystemService.addStarSystem("Test Star System");
        assertEquals("test-star-system", starSystem1.key().value());
        // test read of newly created
        var maybeStarSystem1 = starSystemService.getStarSystemByKey(starSystem1.key());
        assertTrue(maybeStarSystem1.isPresent());
        // test count after test
        var afterStarSystems = starSystemService.getStarSystems().toList();
        assertEquals(6, afterStarSystems.size());
        assertTrue(afterStarSystems.contains(starSystem1));
    }

    @Test
    public void testAddStarSystemKeyCollision() throws Exception {
        var starSystemService = generateTestStarSystemService();
        // create first star system
        var starSystem1 = starSystemService.addStarSystem("Test Star System");
        // test 1st key collision
        var starSystem2 = starSystemService.addStarSystem("Test Star System");
        assertNotEquals(starSystem1.key().value(), starSystem2.key().value());
        // test 2nd key collision
        var starSystem3 = starSystemService.addStarSystem("Test Star System");
        assertNotEquals(starSystem1.key().value(), starSystem3.key().value());
        assertNotEquals(starSystem2.key().value(), starSystem3.key().value());
    }

    @Test
    public void testAddSpaceHighway() throws Exception {
        var starSystemService = generateTestStarSystemService();
        var from = new StarSystemKey("vega");
        var to = new StarSystemKey("sirius");
        var highway = starSystemService.addSpaceHighway(from, to, 1.0);
        assertEquals(1.0, highway.duration());
        var route = starSystemService.calculateFastestRoute(from, to);
        assertEquals(2, route.size());
        var duration = starSystemService.calculateTotalDurationForRoute(route);
        assertEquals(1.0, duration);
    }

    @Test
    public void testAddInvalidSpaceHighwaySameStartAndEnd() throws Exception {
        var starSystemService = generateTestStarSystemService();
        var from = new StarSystemKey("vega");
        var to = new StarSystemKey("vega");
        assertThrows(InvalidRouteException.class, () -> starSystemService.addSpaceHighway(from, to, 1.0));
    }

    @Test
    public void testAddInvalidSpaceHighwayNegativeDuration() throws Exception {
        var starSystemService = generateTestStarSystemService();
        var from = new StarSystemKey("vega");
        var to = new StarSystemKey("sirius");
        assertThrows(InvalidRouteException.class, () -> starSystemService.addSpaceHighway(from, to, -1.0));
    }

    private StarSystemService generateTestStarSystemService() throws Exception {
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
