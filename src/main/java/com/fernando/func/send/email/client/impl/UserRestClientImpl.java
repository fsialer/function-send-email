package com.fernando.func.send.email.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.func.send.email.client.UserRestClient;
import com.fernando.func.send.email.dto.Token;
import com.fernando.func.send.email.dto.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

public class UserRestClientImpl implements UserRestClient {
    private final HttpClient client;

    public UserRestClientImpl(){
        client = HttpClient.newHttpClient();
    }

    @Override
    public User findById(Long userId, String token){
        try{
            HttpRequest userRestClient = HttpRequest.newBuilder()
                    .uri(URI.create(System.getenv("USER_URL").concat("/").concat(userId.toString())))
                    .header("Authorization", "Bearer ".concat(token))
                    .build();
            HttpResponse<String> response =  client.send(userRestClient, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Failed to fetch author, status code: " + response.statusCode());
                return null;
            }
             ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), User.class);
        }catch (IOException | InterruptedException ex){
            LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Interrupted!", ex);
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
