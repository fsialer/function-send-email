package com.fernando.func.send.email.client.impl;



import com.fernando.func.send.email.client.UserRestClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class UserRestClientImpl implements UserRestClient {
    private final HttpClient client;

    public UserRestClientImpl(){
        client = HttpClient.newHttpClient();
    }

    @Override
    public HttpResponse<String> findById(Long userId,String token) {
        try{
            HttpRequest userRestClient = HttpRequest.newBuilder()
                    .uri(URI.create(System.getenv("USER_URL").concat("/").concat(userId.toString())))
                    .header("Authorization", "Bearer ".concat(token))
                    .build();
            return  client.send(userRestClient, HttpResponse.BodyHandlers.ofString());
        }catch (IOException | InterruptedException ex){
            Logger.getLogger(UserRestClientImpl.class.getName()).severe("Error sending request: " + ex.getMessage());
            return null;
        }
    }
}
