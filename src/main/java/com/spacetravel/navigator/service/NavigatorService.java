package com.spacetravel.navigator.service;

import com.spacetravel.navigator.exceptions.NoSuchRouteException;
import com.spacetravel.navigator.model.StarSystemKey;
import com.spacetravel.navigator.repository.SpaceHighwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class NavigatorService {

    private final SpaceHighwayRepository spaceHighwayRepository;

    @Autowired
    public NavigatorService(SpaceHighwayRepository spaceHighwayRepository) {
        this.spaceHighwayRepository = spaceHighwayRepository;
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
     * This function calculates the fastest route between two star-systems using the a* algorithm. If start and end
     * star systems are the same, at least one other star system is visited.
     * @param from star-system key
     * @param to star-system key
     * @return fastest route with at least two star-systems
     * @throws NoSuchRouteException if no route was found
     */
    public List<StarSystemKey> calculateFastestRoute(@NonNull StarSystemKey from, @NonNull StarSystemKey to) throws NoSuchRouteException {
        var open = new PriorityQueue<RouteNode>();
        open.add(new RouteNode(from, 0.0, null));
        var closed = new LinkedList<StarSystemKey>();

        while (!open.isEmpty()) {
            var currentNode = open.remove();
            // the route must have at least one stop
            if (currentNode.previous != null) {
                if (currentNode.starSystem().equals(to)) {
                    // target system found, resolve route from nodes
                    var route = new LinkedList<StarSystemKey>();
                    route.add(currentNode.starSystem());
                    while (currentNode.previous != null) {
                        currentNode = currentNode.previous;
                        route.add(0, currentNode.starSystem());
                    }
                    return route;
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
        // die Open List ist leer, es existiert kein Pfad zum Ziel
        throw new NoSuchRouteException();
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
