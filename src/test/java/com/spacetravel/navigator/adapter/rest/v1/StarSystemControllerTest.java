package com.spacetravel.navigator.adapter.rest.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.spacetravel.navigator.adapter.rest.v1.model.StarSystemRepresentation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

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
                .uri(URI.create("http://localhost:" + port + "/api/v1/star-systems"))
                .build();
        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        var starSystems = new Gson().fromJson(response.body(), StarSystemRepresentation[].class);
        assertEquals(5, starSystems.length);
    }
}
