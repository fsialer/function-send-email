package com.fernando.func.send.email.client;

import java.net.http.HttpResponse;

public interface FollowerRestClient {
    HttpResponse<String> findFollowers(Long authorId,String token);
}
