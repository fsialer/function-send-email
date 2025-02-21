package com.fernando.func.send.email.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.func.send.email.client.AuthorizationRestClient;
import com.fernando.func.send.email.dto.Token;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import static com.azure.core.implementation.http.rest.RestProxyUtils.LOGGER;

public class AuthorizationRestClientImpl implements AuthorizationRestClient {

    private final HttpClient client;

    public AuthorizationRestClientImpl(){
        client = HttpClient.newHttpClient();
    }

    @Override
    public Token obtainToken(String clientId, String secret, String grantType, String scope) {

        try{
            String formData = "grant_type=".concat(grantType).concat("&scope=").concat(scope);
            String auth = clientId.concat(":").concat(secret);
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(System.getenv("AUTH_SERVER_URL")))
                    .header("Authorization", "Basic " + encodedAuth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formData))
                    .build();

            HttpResponse<String> response =  client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Failed to fetch author, status code: " + response.statusCode());
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), Token.class);
        }catch(IOException|InterruptedException ex){
            LOGGER.log(com.azure.core.util.logging.LogLevel.WARNING, () -> "Interrupted!", ex);
            Thread.currentThread().interrupt();
            return null;
        }

    }
}
