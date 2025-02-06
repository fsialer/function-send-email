package com.fernando.func.send.email.client;

import java.net.http.HttpResponse;

public interface AuthorizationRestClient {
    HttpResponse<String> obtainToken(String client, String secret, String grantType, String scope);
}
