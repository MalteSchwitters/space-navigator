package com.spacetravel.navigator.adapter.rest.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.spacetravel.navigator.adapter.rest.v1.model.StarSystemRepresentation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StarSystemControllerTest {

    @Value(value="${local.server.port}")
    private int port;

    @Test
    public void testGetStarSystems() throws Exception {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems"))
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        var starSystems = new Gson().fromJson(response.body(), StarSystemRepresentation[].class);
        assertEquals(5, starSystems.length);
    }

    @Test
    public void testAddStarSystem() throws Exception {
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{ \"name\": \"New Star System\" }"))
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        var starSystem = new Gson().fromJson(response.body(), StarSystemRepresentation.class);
        assertEquals("New Star System", starSystem.getName());
    }

    @Test
    public void testAddStarSystemWithoutName() throws Exception {
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{ \"name_\": \"New Star System\" }"))
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void testAddStarSystemWithEmptyName() throws Exception {
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{ \"name\": \"\" }"))
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void testAddStarSystemInvalidContentType() throws Exception {
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{ \"name\": \"New Star System\" }"))
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "text/plain")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void testGetStarSystemByKey() throws Exception {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems/by-key/sirius"))
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        var starSystem = new Gson().fromJson(response.body(), StarSystemRepresentation.class);
        assertEquals("Sirius", starSystem.getName());
    }

    @Test
    public void testGetStarSystemByKeyNotFound() throws Exception {
        var request = HttpRequest.newBuilder()
                .GET()
                .header(HttpHeaders.ACCEPT, "application/json")
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems/by-key/sirius2"))
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}
