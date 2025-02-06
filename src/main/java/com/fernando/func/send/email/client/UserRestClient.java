package com.fernando.func.send.email.client;

import java.net.http.HttpResponse;

public interface UserRestClient {
    HttpResponse<String> findById(Long userId,String token);
}
