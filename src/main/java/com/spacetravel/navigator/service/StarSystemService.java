package com.spacetravel.navigator.service;

import com.spacetravel.navigator.exceptions.InvalidRouteException;
import com.spacetravel.navigator.exceptions.InvalidStarSystemException;
import com.spacetravel.navigator.exceptions.NoSuchRouteException;
import com.spacetravel.navigator.exceptions.RouteAlreadyExistsException;
import com.spacetravel.navigator.model.*;
import com.spacetravel.navigator.repository.SpaceHighwayRepository;
import com.spacetravel.navigator.repository.StarSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Stream;

@Service
public class StarSystemService {

    private final StarSystemRepository starSystemRepository;
    private final SpaceHighwayRepository spaceHighwayRepository;

    @Autowired
    public StarSystemService(
            StarSystemRepository starSystemRepository,
            SpaceHighwayRepository spaceHighwayRepository
    ) {
        this.starSystemRepository = starSystemRepository;
        this.spaceHighwayRepository = spaceHighwayRepository;
    }

    public Stream<StarSystem> getStarSystems() {
        return starSystemRepository.findAll();
    }

    public Optional<StarSystem> getStarSystemByKey(@NonNull StarSystemKey key) {
        return starSystemRepository.findByKey(key);
    }

    public StarSystem addStarSystem(@NonNull String name) throws InvalidStarSystemException {
        if (name.isBlank()) {
            throw new InvalidStarSystemException("missing name");
        }
        return starSystemRepository.add(name);
    }

    public SpaceHighway addSpaceHighway(@NonNull StarSystemKey from, @NonNull StarSystemKey to, double duration) throws RouteAlreadyExistsException, InvalidRouteException {
        if (from.equals(to)) {
            throw new InvalidRouteException("start and end star systems are equal");
        }
        if (duration < 0) {
            throw new InvalidRouteException("duration must not be negative");
        }
        return spaceHighwayRepository.add(from, to, duration);
    }

    /**
     * This function calculates the duration for the provided route. If a route part has the same start and end
     * star system it is treated as a route with duration 0. If the length of the route is less than two star-systems,
     * the duration is always 0.
     * @param starSystems the route of star systems
     * @return duration in hours
     * @throws NoSuchRouteException if the route is invalid
     */
    public double calculateTotalDurationForRoute(@NonNull List<StarSystemKey> starSystems) throws NoSuchRouteException {
        if (starSystems.size() < 2) {
            return 0.0;
        }
        var duration = 0.0;
        for (int i = 1; i < starSystems.size(); i++) {
            var from = starSystems.get(i - 1);
            var to = starSystems.get(i);
            if (from.equals(to)) {
                // this violates the assumption, that a space highway never has the same start and end star system
                // should this throw an error?
                continue;
            }
            var spaceHighway = spaceHighwayRepository.findByStartAndEndSystem(from, to)
                    .orElseThrow(NoSuchRouteException::new);
            duration += spaceHighway.duration();
        }
        return duration;
    }

    /**
     * Calculate all known routes between the 'from' star-system and the 'to' star-system that match the given filter
     * @param from start star-system
     * @param to end star-system
     * @param filter Filter of minimum steps, maximum steps or maximum duration
     * @return a list of all routes
     */
    public List<Route> calculateAllRoutes(@NonNull StarSystemKey from, @NonNull StarSystemKey to, @NonNull RouteFilter filter) {
        return calculateAllRoutes(from, to, filter, 0.0);
    }

    private List<Route> calculateAllRoutes(@NonNull StarSystemKey from, @NonNull StarSystemKey to, @NonNull RouteFilter filter, double duration) {
        // validate cancel conditions to prevent endless loops
        if (filter.maxSteps() == null && filter.maxDuration() == null) {
            throw new IllegalArgumentException("At least one end condition must be specified!");
        }

        // cancel condition: max duration exceeded
        if (filter.maxDuration() != null && duration > filter.maxDuration()) {
            return Collections.emptyList();
        }

        // cancel condition: maximum depth reached
        if (filter.maxSteps() != null && filter.maxSteps() <= 0) {
            return Collections.emptyList();
        }

        List<Route> routes = new LinkedList<>();
        var spaceHighways = this.spaceHighwayRepository
            .findByStartSystem(from)
            .toList();
        // we found our target system
        if (filter.minSteps() == null || filter.minSteps() <= 1) {
            for (var spaceHighway : spaceHighways) {
                if (spaceHighway.to().equals(to)) {
                    var totalDuration = duration + spaceHighway.duration();
                    // only add route to matches if don't exceed the max duration filter
                    if (filter.maxDuration() == null || totalDuration < filter.maxDuration()) {
                        routes.add(new Route(List.of(from, to), totalDuration));
                    }
                    break;
                }
            }
        }

        // recursively test all reachable star systems
        for (var spaceHighway : spaceHighways) {
            var newFilter = new RouteFilter(
                    filter.minSteps() != null ? filter.minSteps() - 1: null,
                    filter.maxSteps() != null ? filter.maxSteps() -1 : null,
                    filter.maxDuration() != null ? filter.maxDuration() : null);
            var newDuration = duration + spaceHighway.duration();
            for (var subRoute : calculateAllRoutes(spaceHighway.to(), to, newFilter, newDuration)) {
                List<StarSystemKey> starSystems = new LinkedList<>();
                starSystems.add(from);
                starSystems.addAll(subRoute.starSystems());
                routes.add(new Route(starSystems, subRoute.duration()));
            }
        }
        return routes;
    }

    /**
     * This function calculates the fastest route between two star-systems using the a* algorithm. If start and end
     * star systems are the same, at least one other star system is visited.
     * @param from star-system key
     * @param to star-system key
     * @return an optional of the fastest route with at least two star-systems
     */
    public Optional<Route> calculateFastestRoute(@NonNull StarSystemKey from, @NonNull StarSystemKey to) {
        var open = new PriorityQueue<RouteNode>();
        open.add(new RouteNode(from, 0.0, null));
        var closed = new LinkedList<StarSystemKey>();

        while (!open.isEmpty()) {
            var currentNode = open.remove();
            // the route must have at least one stop
            if (currentNode.previous != null) {
                if (currentNode.starSystem().equals(to)) {
                    // target system found, resolve route from nodes
                    var starSystems = new LinkedList<StarSystemKey>();
                    var cost = currentNode.cost();
                    starSystems.add(currentNode.starSystem());
                    while (currentNode.previous != null) {
                        currentNode = currentNode.previous;
                        starSystems.add(0, currentNode.starSystem());
                    }
                    return Optional.of(new Route(starSystems, cost));
                }
                closed.add(currentNode.starSystem());
            }

            var successors = this.spaceHighwayRepository
                    .findByStartSystem(currentNode.starSystem())
                    .toList();
            for (var successor : successors) {
                if (closed.contains(successor.to())) {
                    // successor is already on closed list
                    continue;
                }
                // calculate cost to get to this node
                var cost  = currentNode.cost() + successor.duration();
                var maybeExistingRoute = open.stream()
                        .filter(it -> it.starSystem().equals(successor.to()))
                        .findFirst();
                if (maybeExistingRoute.isPresent() && cost > maybeExistingRoute.get().cost()) {
                    // successor is already on open list with a shorter path
                    continue;
                }
                maybeExistingRoute.ifPresent(open::remove);
                open.add(new RouteNode(successor.to(), cost, currentNode));
            }

        }
        // open list is empty, there is no path to the target
        return Optional.empty();
    }

    private record RouteNode(
            @NonNull StarSystemKey starSystem,
            double cost,
            @Nullable RouteNode previous
    ) implements Comparable<RouteNode> {

        @Override
        public int compareTo(RouteNode b) {
            return Double.compare(this.cost, b.cost);
        }
    }
}
