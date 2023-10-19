package com.spacetravel.navigator.adapter.rest.v1;

import com.google.gson.Gson;
import com.spacetravel.navigator.adapter.rest.v1.model.RouteDurationRepresentation;
import com.spacetravel.navigator.adapter.rest.v1.model.StarSystemRepresentation;
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
        var starSystems = new Gson().fromJson(response.body(), String[].class);
        assertEquals(3, starSystems.length);
        assertEquals("solar-system", starSystems[0]);
        assertEquals("alpha-centauri", starSystems[1]);
        assertEquals("sirius", starSystems[2]);
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
        var duration = new Gson().fromJson(response.body(), RouteDurationRepresentation.class);
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
