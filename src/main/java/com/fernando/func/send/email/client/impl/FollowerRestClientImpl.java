package com.fernando.func.send.email.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.func.send.email.client.FollowerRestClient;
import com.fernando.func.send.email.dto.Follower;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

public class FollowerRestClientImpl implements FollowerRestClient {
    private final HttpClient client;

    public FollowerRestClientImpl(){
        client = HttpClient.newHttpClient();
    }

    @Override
    public List<Follower> findFollowers(Long authorId, String token) {
        try {
            HttpRequest userRestClient = HttpRequest.newBuilder()
                    .uri(URI.create(System.getenv("FOLLOWER_URL").concat("/").concat(authorId.toString())))
                    .header("Authorization", "Bearer ".concat(token))
                    .build();
            HttpResponse<String> response =client.send(userRestClient, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Failed to fetch author, status code: " + response.statusCode());
                return Collections.emptyList();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<List<Follower>>() {});
        }catch(IOException|InterruptedException ex){
            LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Interrupted!", ex);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }
}
