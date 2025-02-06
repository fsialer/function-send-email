package com.fernando.func.send.email.client.impl;

import com.fernando.func.send.email.client.AuthorizationRestClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.logging.Logger;

public class AuthorizationRestClientImpl implements AuthorizationRestClient {

    private final HttpClient client;

    public AuthorizationRestClientImpl(){
        client = HttpClient.newHttpClient();
    }
    @Override
    public HttpResponse<String> obtainToken(String clientId, String secret, String grantType, String scope){
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
            return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        }catch(IOException| InterruptedException ex){
            Logger.getLogger(AuthorizationRestClientImpl.class.getName()).severe("Error sending request: " + ex.getMessage());
            return null;
        }
    }
}
