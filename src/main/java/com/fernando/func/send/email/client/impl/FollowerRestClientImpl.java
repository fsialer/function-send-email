package com.fernando.func.send.email.client.impl;

import com.fernando.func.send.email.client.FollowerRestClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class FollowerRestClientImpl implements FollowerRestClient {
    private final HttpClient client;

    public FollowerRestClientImpl(){
        client = HttpClient.newHttpClient();
    }

    @Override
    public HttpResponse<String> findFollowers(Long authorId,String token) {
        try {
            HttpRequest userRestClient = HttpRequest.newBuilder()
                    .uri(URI.create(System.getenv("FOLLOWER_URL").concat("/").concat(authorId.toString())))
                    .header("Authorization", "Bearer ".concat(token))
                    .build();
            return client.send(userRestClient, HttpResponse.BodyHandlers.ofString());
        }catch(IOException|InterruptedException ex){
            Logger.getLogger(FollowerRestClientImpl.class.getName()).severe("Error sending request: " + ex.getMessage());
            return null;
        }
    }
}
