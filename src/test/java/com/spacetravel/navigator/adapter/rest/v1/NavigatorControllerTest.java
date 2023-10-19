package com.spacetravel.navigator.adapter.rest.v1;

import com.google.gson.Gson;
import com.spacetravel.navigator.adapter.rest.v1.model.RouteRepresentation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NavigatorControllerTest {

    @Value(value="${local.server.port}")
    private int port;

    @Test
    public void testGetShortestPathBetweenStarSystems() throws Exception {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + port + "/api/v1/navigator/fastest-route/from/solar-system/to/sirius"))
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        var route = new Gson().fromJson(response.body(), RouteRepresentation.class);
        assertEquals(3, route.getStarSystems().size());
        assertEquals("solar-system", route.getStarSystems().get(0));
        assertEquals("alpha-centauri", route.getStarSystems().get(1));
        assertEquals("sirius", route.getStarSystems().get(2));
    }

    @Test
    public void testGetRouteDuration() throws Exception {
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("[\"solar-system\",\"alpha-centauri\",\"sirius\"]"))
                .uri(URI.create("http://localhost:" + port + "/api/v1/navigator/route-duration"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        var duration = new Gson().fromJson(response.body(), RouteRepresentation.class);
        assertEquals(9.0, duration.getDuration());
    }

    @Test
    public void testGetRouteDurationNoSuchRoute() throws Exception {
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("[\"vega\",\"sirius\"]"))
                .uri(URI.create("http://localhost:" + port + "/api/v1/navigator/route-duration"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}
