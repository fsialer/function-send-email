package com.fernando.func.send.email.client;

import com.fernando.func.send.email.dto.Token;

public interface AuthorizationRestClient {
    Token obtainToken(String client, String secret, String grantType, String scope);
}
