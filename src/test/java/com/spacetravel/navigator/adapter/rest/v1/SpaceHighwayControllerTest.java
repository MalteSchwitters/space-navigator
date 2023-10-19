package com.spacetravel.navigator.adapter.rest.v1;

import com.google.gson.Gson;
import com.spacetravel.navigator.adapter.rest.v1.model.CreateSpaceHighwayRequest;
import com.spacetravel.navigator.adapter.rest.v1.model.SpaceHighwayRepresentation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpaceHighwayControllerTest {

    @Value(value="${local.server.port}")
    private int port;

    @Test
    public void testAddSpaceHighway() throws Exception {
        CreateSpaceHighwayRequest requestBody = new CreateSpaceHighwayRequest();
        requestBody.setFromStarSystemKey("vega");
        requestBody.setToStarSystemKey("solar-system");
        requestBody.setDuration(9.0);

        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(requestBody)))
                .uri(URI.create("http://localhost:" + port + "/api/v1/space-highways"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        var spaceHighway = new Gson().fromJson(response.body(), SpaceHighwayRepresentation.class);
        assertEquals("vega", spaceHighway.getFromStarSystemKey());
        assertEquals("solar-system", spaceHighway.getToStarSystemKey());
        assertEquals(9.0, spaceHighway.getDuration());
    }

    @Test
    public void testAddSpaceHighwayDuplicateRoute() throws Exception {
        CreateSpaceHighwayRequest requestBody = new CreateSpaceHighwayRequest();
        requestBody.setFromStarSystemKey("sirius");
        requestBody.setToStarSystemKey("vega");
        requestBody.setDuration(9.0);

        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(requestBody)))
                .uri(URI.create("http://localhost:" + port + "/api/v1/space-highways"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(409, response.statusCode());
    }

    @Test
    public void testAddSpaceHighwayNegativeDuration() throws Exception {
        CreateSpaceHighwayRequest requestBody = new CreateSpaceHighwayRequest();
        requestBody.setFromStarSystemKey("vega");
        requestBody.setToStarSystemKey("solar-system");
        requestBody.setDuration(-9.0);

        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(requestBody)))
                .uri(URI.create("http://localhost:" + port + "/api/v1/space-highways"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void testAddSpaceHighwaySameStartAndEnd() throws Exception {
        CreateSpaceHighwayRequest requestBody = new CreateSpaceHighwayRequest();
        requestBody.setFromStarSystemKey("vega");
        requestBody.setToStarSystemKey("vega");
        requestBody.setDuration(-9.0);

        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(requestBody)))
                .uri(URI.create("http://localhost:" + port + "/api/v1/space-highways"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }
}
